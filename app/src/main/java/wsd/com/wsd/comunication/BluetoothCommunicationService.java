package wsd.com.wsd.comunication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import wsd.com.wsd.comunication.handlers.OnMessageHendlerImpl;
import wsd.com.wsd.comunication.utils.JobType;
import wsd.com.wsd.singletons.DeviceSingleton;


public class BluetoothCommunicationService {

    // Debugging
    private static final String TAG = "ConnectionService";

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    private static final String FIRST_CONNECTION_VERIFICATION_MESSAGE = "FCVM";
    private static final String SECOND_CONNECTION_VERIFICATION_MESSAGE = "SCVM";
    private static final String THIRD_CONNECTION_VERIFICATION_MESSAGE = "TCVM";
    private static final String CONFIRM_CONNECTION_VERIFICATION_MESSAGE = "CONFIRM";
    private static final String FINISH_CONNECTION_VERIFICATION_MESSAGE = "FINISH";
    private static final String OTHER_CONNECTION_VERIFICATION_MESSAGE = "OTHER";
    private byte[] firstConnectionVerificationMessageInBytes = FIRST_CONNECTION_VERIFICATION_MESSAGE.getBytes();
    private byte[] secondConnectionVerificationMessageInBytes = SECOND_CONNECTION_VERIFICATION_MESSAGE.getBytes();
    private byte[] thirdConnectionVerificationMessageInBytes = THIRD_CONNECTION_VERIFICATION_MESSAGE.getBytes();
    private byte[] confirmConnectionVerificationMessageInBytes = CONFIRM_CONNECTION_VERIFICATION_MESSAGE.getBytes();
    private byte[] finishConnectionVerificationMessageInBytes = FINISH_CONNECTION_VERIFICATION_MESSAGE.getBytes();
    private byte[] otherConnectionVerificationMessageInBytes = OTHER_CONNECTION_VERIFICATION_MESSAGE.getBytes();

    // Message types sent from the BluetoothCommunicationService Handler
    public static final int MESSAGE_CONFIRM = 1;
    public static final int MESSAGE_FINISH = 2;

    // Key names received from the BluetoothCommunicationService Handler
    public static final String CONFIRM = "confirm";
    public static final String FINISH = "finish";

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothSecureConnection";

    // Unique UUID for this application
    private static final UUID UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    // Member fields
    private final BluetoothAdapter bluetoothAdapter;
    private Handler handler;
    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private int state;
    private boolean broadcasting = false;
    private static BluetoothDevice currentDevice = null;
    private Timer repeatTimer = new Timer();
    private static boolean repeatLock = false;
    private static boolean connected = false;
    private OnMessageHendlerImpl hendler;

    private String message;


    /**
     * Constructor. Prepares a new BluetoothChat session.
     */
    public BluetoothCommunicationService() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        state = STATE_NONE;
    }

    public synchronized void sendMessage(String deviceAddress, String message) {
        this.message = message;
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        setCurrentDevice(device);
        startBroadcasting();
        connect(device);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private void setCurrentDevice(BluetoothDevice device) {
        currentDevice = device;
    }

    private void startBroadcasting() {
        broadcasting = true;
    }

    private void stopBroadcasting() {
        broadcasting = false;
    }

    private synchronized void setConnected(boolean state) {
        connected = state;
    }

    private synchronized boolean isConnected() {
        return connected;
    }

    private synchronized void setRepeatLock(boolean state) {
        repeatLock = state;
    }

    private synchronized boolean getRepeatLock() {
        return repeatLock;
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        Log.d(TAG, "setState() " + this.state + " -> " + state);
        this.state = state;
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return state;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        setConnected(false);

        // Cancel any thread attempting to make a connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        setState(STATE_LISTEN);

        // Start the thread to listen on a BluetoothServerSocket
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }

        if (broadcasting) {
            setState(STATE_NONE);
            if (getRepeatLock() == false) {
                setRepeatLock(true);
                repeatTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        setState(STATE_CONNECTING);
                        setRepeatLock(false);
                        connect(currentDevice);
                    }
                }, 3000);
            }
        }
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    private synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (state == STATE_CONNECTING && connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // Start the thread to connect with the given device
        connectThread = new ConnectThread(device);
        connectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        setState(STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (state != STATE_CONNECTED) return;
            r = connectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    public void resetConnection() {
        BluetoothCommunicationService.this.start();
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, UUID_SECURE);
            } catch (IOException e) {
                Log.e(TAG, "Socket listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "Socket BEGIN mAcceptThread" + this);
            setName("AcceptThreadSecure");

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (state != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothCommunicationService.this) {
                        switch (state) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            Log.i(TAG, "END mAcceptThread");
        }

        public void cancel() {
            Log.d(TAG, "Socket cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket close() of server failed", e);
            }
        }
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID_SECURE);
            } catch (IOException e) {
                Log.e(TAG, "Socket create() failed", e);
            }

            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN connectThread");
            setName("ConnectThreadSecure");

            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                resetConnection();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothCommunicationService.this) {
                connectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private InputStream mmInStream;
        private OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN connectedThread");
            final byte[] buffer = new byte[2048];
            int bytes;
            boolean lock = false;
            boolean lock2 = false;
            boolean lock3 = false;
            boolean lock4 = true;

            write(firstConnectionVerificationMessageInBytes);

            Timer cancelTimer = new Timer();
            cancelTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    resetConnection();
                }
            }, 500);

            // Keep listening to the InputStream while connected
            while (state == STATE_CONNECTED) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);


                    if(lock3 == true && broadcasting == false) {
                        AgentMessage agent = CommunicatParser.convertStringToACLMessage(readMessage);
                        hendler = new OnMessageHendlerImpl();
                        hendler.handle(agent);
                        lock3 = false;
                        if (agent.getJobType() == JobType.NETWORK_PROPAGATION) {
                            write(confirmConnectionVerificationMessageInBytes);
                        } else if (agent.getJobType() == JobType.CONFIRM_EVENT) {
                            write(finishConnectionVerificationMessageInBytes);
                        } else {
                            write(otherConnectionVerificationMessageInBytes);
                        }
                    }

                    if(lock4 == true && broadcasting == true) {
                        if(readMessage.equals(CONFIRM_CONNECTION_VERIFICATION_MESSAGE)) {
                            stopBroadcasting();
                            lock4 = false;
                            Message msg1 = handler.obtainMessage(MESSAGE_CONFIRM);
                            Bundle bundle = new Bundle();
                            bundle.putString(CONFIRM, "confirm");
                            msg1.setData(bundle);
                            handler.sendMessage(msg1);
                        } else if (readMessage.equals(FINISH_CONNECTION_VERIFICATION_MESSAGE)) {
                            stopBroadcasting();
                            lock4 = false;
                            Message msg2 = handler.obtainMessage(MESSAGE_FINISH);
                            Bundle bundle = new Bundle();
                            bundle.putString(FINISH, "finish");
                            msg2.setData(bundle);
                            handler.sendMessage(msg2);
                        } else if (readMessage.equals(OTHER_CONNECTION_VERIFICATION_MESSAGE)) {
                            stopBroadcasting();
                            lock4 = false;
                        }
                    }

                    if(lock == true && lock2 == true) {
                        if(readMessage.equals(THIRD_CONNECTION_VERIFICATION_MESSAGE)) {
                            cancelTimer.cancel();
                            setConnected(true);
                            lock3 = true;
                            if(broadcasting == true) {
                                Log.e(TAG, message);
                                byte[] messageInBytes = message.getBytes();
                                write(messageInBytes);
                            }
                        } else {
                            resetConnection();
                            break;
                        }
                    }

                    if(lock == true && lock2 == false) {
                        if(!readMessage.equals(SECOND_CONNECTION_VERIFICATION_MESSAGE)) {
                            cancelTimer.cancel();
                            resetConnection();
                            break;
                        }
                        write(thirdConnectionVerificationMessageInBytes);
                        lock2 = true;
                    }

                    if(lock == false) {
                        if(!readMessage.equals(FIRST_CONNECTION_VERIFICATION_MESSAGE)) {
                            cancelTimer.cancel();
                            resetConnection();
                            break;
                        }
                        write(secondConnectionVerificationMessageInBytes);
                        lock = true;
                    }

                    String tete = new String(buffer, 0, bytes);
                    Log.e(TAG, tete);
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    resetConnection();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
//            if (mmInStream != null) {
//                try {mmInStream.close();} catch (Exception e) {}
//                mmInStream = null;
//            }
//
//            if (mmOutStream != null) {
//                try {mmOutStream.close();} catch (Exception e) {}
//                mmOutStream = null;
//            }

            if (mmSocket != null) {
                try {
                    mmSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "close() of connect socket failed", e);
                }
            }
        }
    }
}


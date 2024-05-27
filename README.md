# Chat Application

## Overview

This repository contains two chat applications: `ChatAppTCP` and `ChatAppUDP`. Both applications allow multiple users to communicate in real-time via a graphical user interface (GUI). 

- **ChatAppTCP**: Uses the TCP protocol to ensure reliable communication.
- **ChatAppUDP**: Uses the UDP protocol for faster, but less reliable communication.

## Features

- User registration and login.
- Real-time messaging.
- Display of connected users.
- User-friendly GUI.
- Server-side logging and control panel for the TCP application.

## Requirements

- Java SE Development Kit (JDK) 8 or higher.
- Network connection.

## Usage

### ChatAppTCP

The TCP-based chat application provides a reliable messaging service, ensuring messages are delivered in the correct order and without loss.

#### Client

1. Run `ClientMain.java`.
2. Enter your username in the prompted dialog.
3. The client connects to the server at `localhost` on port `8000`.
4. Use the GUI to send messages and see the list of connected users.

#### Server

1. Run `ServerMain.java`.
2. Start the server using the "Start Server" button in the GUI.
3. Monitor connected users and server status.
4. Optionally, stop the server using the "Stop Server" button.

#### Directory Structure

- `Client.java`: Manages the client's connection, sending and receiving messages.
- `ClientGUI.java`: Provides the client's GUI.
- `ClientMain.java`: Entry point for the client application.
- `Server.java`: Manages client connections and broadcasts messages.
- `ServerGUI.java`: Provides the server's GUI.
- `ServerListener.java`: Handles communication with individual clients.
- `ServerMain.java`: Entry point for the server application.
- `User.java`: Represents a user and maintains a list of connected users.

### ChatAppUDP

The UDP-based chat application provides a faster messaging service, but without the guarantees of message order and delivery.

#### Client

1. Run `Main.java` file to start up the client.
2. Enter your username in the prompted dialog.
3. The client connects to the server at `localhost` on port `8000`.
4. Use the GUI to send messages and see the list of connected users.

#### Directory Structure

- `GUI.java`: Provides the client's GUI.
- `Main.java`: Entry point for the client application.
- `NetworkReceiver.java`: Handles receiving messages from the server using UDP.
- `NetworkSender.java`: Handles sending messages to the server using UDP.
- `User.java`: Represents a user and maintains a list of connected users.

## Notes

- Ensure the server is running before starting the clients.
- The applications are configured to connect to `localhost` on port `8000`. Modify the source code if a different address or port is required.
- Both TCP and UDP applications use the same `User.java` class for representing users.


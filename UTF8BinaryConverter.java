import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UTF8BinaryConverter {

    // Convert a string to binary UTF-8 representation
    public static String encodeToBinary(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        StringBuilder binaryString = new StringBuilder();

        for (byte b : bytes) {
            binaryString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        return binaryString.toString();
    }

    // Convert binary string back to original text
    public static String decodeFromBinary(String binaryMessage) {
        StringBuilder decodedMessage = new StringBuilder();

        for (int i = 0; i < binaryMessage.length(); i += 8) {
            String byteChunk = binaryMessage.substring(i, Math.min(i + 8, binaryMessage.length()));
            int charCode = Integer.parseInt(byteChunk, 2);
            decodedMessage.append((char) charCode);
        }

        return decodedMessage.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Encrypt (Text to Binary)");
            System.out.println("2. Decrypt (Binary to Text)");
            System.out.println("3. Exit");

            String input;
            int choice = -1;

            // Keep asking for valid input
            while (true) {
                System.out.print("Enter your choice (1-3): ");
                input = scanner.nextLine().trim();

                if (input.matches("[1-3]")) {
                    choice = Integer.parseInt(input);
                    break; // Exit loop if valid
                } else {
                    System.out.println("❌ Invalid choice! Please enter 1, 2, or 3.");
                }
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter a message to encrypt: ");
                    String message = scanner.nextLine();
                    String encodedBinary = encodeToBinary(message);
                    System.out.println("✅ Encoded Binary (UTF-8): " + encodedBinary);
                    break;

                case 2:
                    String binaryInput;
                    while (true) {
                        System.out.print("Enter the binary message to decrypt: ");
                        binaryInput = scanner.nextLine().trim();

                        // Validate binary input (must be only 0s and 1s, and a multiple of 8)
                        if (binaryInput.matches("[01]+") && binaryInput.length() % 8 == 0) {
                            break; // Valid input
                        } else {
                            System.out.println("❌ Invalid binary input! Must contain only 0s and 1s in multiples of 8.");
                        }
                    }

                    String decodedMessage = decodeFromBinary(binaryInput);
                    System.out.println("✅ Decoded Message: " + decodedMessage);
                    break;

                case 3:
                    System.out.println("👋 Exiting program...");
                    scanner.close();
                    return; // Exit the loop
            }
        }
    }
}

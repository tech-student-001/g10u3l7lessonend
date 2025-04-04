import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SecureUTF8BinaryConverter {

    private static final String PREFIX = "SECURE-";
    private static final String SUFFIX = "-OVER";
    private static final String SPACE_REPLACEMENT = "XSPACE"; // Hide spaces in encoding

    // Convert a string to binary UTF-8 representation with added security
    public static String encodeToBinary(String message) {
        message = PREFIX + message.replace(" ", SPACE_REPLACEMENT) + SUFFIX; // Add security layer
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        StringBuilder binaryString = new StringBuilder();

        for (byte b : bytes) {
            binaryString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        return binaryString.toString();
    }

    // Convert binary string back to original text with verification
    public static String decodeFromBinary(String binaryMessage) {
        StringBuilder decodedMessage = new StringBuilder();

        for (int i = 0; i < binaryMessage.length(); i += 8) {
            String byteChunk = binaryMessage.substring(i, Math.min(i + 8, binaryMessage.length()));
            int charCode = Integer.parseInt(byteChunk, 2);
            decodedMessage.append((char) charCode);
        }

        String finalMessage = decodedMessage.toString();

        // Security Check: Ensure it starts with "SECURE-" and ends with "-OVER"
        if (!finalMessage.startsWith(PREFIX) || !finalMessage.endsWith(SUFFIX)) {
            return "❌ ERROR: Invalid or tampered message!";
        }

        // Remove security markers and restore spaces
        return finalMessage.substring(PREFIX.length(), finalMessage.length() - SUFFIX.length()).replace(SPACE_REPLACEMENT, " ");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Encrypt (Text to Secure Binary)");
            System.out.println("2. Decrypt (Secure Binary to Text)");
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
                    System.out.println("✅ Secure Encoded Binary (UTF-8): " + encodedBinary);
                    break;

                case 2:
                    String binaryInput;
                    while (true) {
                        System.out.print("Enter the secure binary message to decrypt: ");
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
                    System.out.println("👋 Exiting program. Goodbye!");
                    scanner.close();
                    return; // Exit the loop
            }
        }
    }
}

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class PasswordGenerator {
    private static final int PASSWORD_LENGTH = 12;  // You can adjust the length as needed

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Generate a strong encryption key
        SecretKey secretKey = generateEncryptionKey();

        while (true) {
            System.out.println("\nPassword Generator Menu:");
            System.out.println("1. Generate Password");
            System.out.println("2. Exit");

            System.out.print("Enter your choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            switch (choice) {
                case 1:
                    // Step 2: Generate a strong random password
                    String generatedPassword = generatePassword();
                    System.out.println("Generated Password: " + generatedPassword);

                    // Step 3: Encrypt and store the password
                    String encryptedPassword = encryptPassword(generatedPassword, secretKey);
                    System.out.println("Encrypted Password: " + encryptedPassword);
                    break;

                case 2:
                    System.out.println("Exiting Password Generator. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }

    private static SecretKey generateEncryptionKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);  // Using AES with a key size of 256 bits
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error generating encryption key: " + e.getMessage());
            System.exit(1);
        }
        return null;  // This line will never be reached
    }

    private static String generatePassword() {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()-=_+[]{}|;':,.<>?";

        String allCharacters = uppercaseLetters + lowercaseLetters + digits + specialCharacters;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(allCharacters.length());
            password.append(allCharacters.charAt(randomIndex));
        }

        return password.toString();
    }

    private static String encryptPassword(String password, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.err.println("Error encrypting password: " + e.getMessage());
            System.exit(1);
        }
        return null;  // This line will never be reached
    }
}

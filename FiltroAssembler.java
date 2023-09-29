import java.io.*;
import java.util.Scanner;

public class FiltroAssembler {
    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            System.out.println("Digite o nome do arquivo de entrada:");
            String inputFile = s.nextLine();
            System.out.println("Digite o nome do arquivo de saída:");
            String outputFile = s.nextLine();

            int outputFormat;
            do {
                System.out.println("Escolha o formato de saída:");
                System.out.println("1 - Decimal");
                System.out.println("2 - Hexadecimal");
                System.out.println("3 - Binário");
                outputFormat = s.nextInt();
            } while (outputFormat < 1 || outputFormat > 3);

            try {
                try (InputStream inputStream = new FileInputStream(inputFile)) {
                    OutputStream outputStream = new FileOutputStream(outputFile);

                    int byteRead = -1, contador_bytes = 0, contador_linha = 0;
                    String conteudo = "";
                    while ((byteRead = inputStream.read()) != -1) {
                        contador_bytes = contador_bytes + 1;
                        String formattedByte = formatByte(byteRead, outputFormat);

                        if (contador_bytes == 1) {
                            conteudo = conteudo + "db " + formattedByte + ",";
                        } else if (contador_bytes % 8 == 0) {
                            contador_linha++;
                            conteudo = conteudo + formattedByte + " ; linha " + (contador_linha - 1) + " \ndb ";
                        } else {
                            conteudo = conteudo + formattedByte + ", ";
                        }
                        outputStream.write(conteudo.getBytes());
                        conteudo = "";
                    }
                    outputStream.close();
                }
                System.out.println("Arquivo gerado com sucesso.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String formatByte(int byteValue, int format) {
        switch (format) {
            case 1: // Decimal
                return Integer.toString(byteValue);
            case 2: // Hexadecimal
                return String.format("0x%02X", byteValue);
            case 3: // Binário
                return String.format("%8s", Integer.toBinaryString(byteValue)).replace(' ', '0');
            default:
                return "";
        }
    }
}

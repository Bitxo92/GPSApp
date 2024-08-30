package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CoordenadasYMapa {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Coordenadas y Mapa");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new BorderLayout());

            // Crear panel y componentes
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 1));

            JLabel coordinatesLabel = new JLabel("Coordenadas:");
            JTextArea coordinatesArea = new JTextArea();
            coordinatesArea.setEditable(false);
            JButton mapButton = new JButton("Ver en Google Maps");

            panel.add(coordinatesLabel);
            panel.add(new JScrollPane(coordinatesArea));
            panel.add(mapButton);

            frame.add(panel, BorderLayout.CENTER);

            // Obtener coordenadas y actualizar UI
            try {
                // Obtener coordenadas
                JSONObject json = obtenerCoordenadas();
                double lat = json.getDouble("lat");
                double lon = json.getDouble("lon");

                coordinatesArea.setText("Latitud: " + lat + "\nLongitud: " + lon);

                // Configurar el botón para abrir Google Maps
                String mapUrl = "https://www.google.com/maps?q=" + lat + "," + lon;
                mapButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Desktop.getDesktop().browse(new URL(mapUrl).toURI());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                coordinatesArea.setText("Error al obtener coordenadas.");
            }

            frame.setVisible(true);
        });
    }

    private static JSONObject obtenerCoordenadas() throws Exception {
        String urlString = "http://ip-api.com/json"; // API para obtener coordenadas basadas en IP
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        try (Reader reader = new InputStreamReader(connection.getInputStream())) {
            // Parsear la respuesta JSON
            StringBuilder response = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                response.append((char) c);
            }
            JSONObject jsonObject = new JSONObject(response.toString());
            return jsonObject;
        }
    }
}

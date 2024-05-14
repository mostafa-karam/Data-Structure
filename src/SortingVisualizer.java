import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

public class SortingVisualizer extends JFrame {
    private static final int ARRAY_SIZE = 1000;
    private static final int MAX_VALUE = 1000;
    private int[] array;
    private SortingPanel sortingPanel;
    private JComboBox<String> algorithmComboBox;
    private JButton startButton;
    private JLabel runtimeLabel;
    private JLabel comparisonsLabel;
    private JLabel interchangesLabel;

    public SortingVisualizer() {
        setTitle("Sorting Algorithm Visualizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        array = generateRandomArray(ARRAY_SIZE);
        sortingPanel = new SortingPanel(array);

        algorithmComboBox = new JComboBox<>(new String[]{"Linear Sort", "Bubble Sort", "Quick Sort"});
        startButton = new JButton("Start");
        runtimeLabel = new JLabel("Runtime: ");
        comparisonsLabel = new JLabel("Comparisons: ");
        interchangesLabel = new JLabel("Interchanges: ");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSorting();
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Algorithm: "));
        controlPanel.add(algorithmComboBox);
        controlPanel.add(startButton);
        controlPanel.add(runtimeLabel);
        controlPanel.add(comparisonsLabel);
        controlPanel.add(interchangesLabel);

        add(sortingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void startSorting() {
        new Thread(() -> {
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            int[] arrayCopy = Arrays.copyOf(array, array.length);
            SortingStats stats = null;

            long startTime = System.nanoTime();
            switch (algorithm) {
                case "Linear Sort":
                    stats = LinearSort(arrayCopy);
                    break;
                case "Bubble Sort":
                    stats = bubbleSort(arrayCopy);
                    break;
                case "Quick Sort":
                    stats = quickSort(arrayCopy);
                    break;
            }
            long endTime = System.nanoTime();

            final SortingStats finalStats = stats;
            final long finalTime = endTime - startTime;

            SwingUtilities.invokeLater(() -> {
                runtimeLabel.setText("Runtime: " + finalTime / 1e6 + " ms");
                comparisonsLabel.setText("Comparisons: " + finalStats.comparisons);
                interchangesLabel.setText("Interchanges: " + finalStats.interchanges);
                sortingPanel.setArray(arrayCopy);
                sortingPanel.repaint();
            });
        }).start();
    }

    private int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(MAX_VALUE);
        }
        return array;
    }

    private SortingStats LinearSort(int[] array) {
        int comparisons = 0;
        int interchanges = 0;
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                comparisons++;
                array[j + 1] = array[j];
                j = j - 1;
                interchanges++;
                updateArray(array);
            }
            array[j + 1] = key;
            interchanges++;
            updateArray(array);
        }
        return new SortingStats(comparisons, interchanges);
    }

    private SortingStats bubbleSort(int[] array) {
        int comparisons = 0;
        int interchanges = 0;
        boolean swapped;
        for (int i = 0; i < array.length - 1; i++) {
            swapped = false;
            for (int j = 0; j < array.length - i - 1; j++) {
                comparisons++;
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                    interchanges++;
                    updateArray(array);
                }
            }
            if (!swapped) break;
        }
        return new SortingStats(comparisons, interchanges);
    }

    private SortingStats quickSort(int[] array) {
        return quickSort(array, 0, array.length - 1, new SortingStats(0, 0));
    }

    private SortingStats quickSort(int[] array, int low, int high, SortingStats stats) {
        if (low < high) {
            int pi = partition(array, low, high, stats);
            quickSort(array, low, pi - 1, stats);
            quickSort(array, pi + 1, high, stats);
        }
        return stats;
    }

    private int partition(int[] array, int low, int high, SortingStats stats) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            stats.comparisons++;
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
                stats.interchanges++;
                updateArray(array);
            }
        }
        swap(array, i + 1, high);
        stats.interchanges++;
        updateArray(array);
        return i + 1;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void updateArray(int[] array) {
        sortingPanel.setArray(array);
        sortingPanel.repaint();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class SortingStats {
        int comparisons;
        int interchanges;

        SortingStats(int comparisons, int interchanges) {
            this.comparisons = comparisons;
            this.interchanges = interchanges;
        }
    }

    private static class SortingPanel extends JPanel {
        private int[] array;

        public SortingPanel(int[] array) {
            this.array = array;
        }

        public void setArray(int[] array) {
            this.array = array;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (array != null) {
                int width = getWidth();
                int height = getHeight();
                int barWidth = width / array.length;
                for (int i = 0; i < array.length; i++) {
                    int barHeight = (int) ((double) array[i] / MAX_VALUE * height);
                    g.fillRect(i * barWidth, height - barHeight, barWidth, barHeight);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortingVisualizer visualizer = new SortingVisualizer();
            visualizer.setVisible(true);
        });
    }
}

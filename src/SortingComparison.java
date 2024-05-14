import java.util.Random;

public class SortingComparison {

    public static void main(String[] args) {
        int arraySize = 10000;
        int[] randomArray = generateRandomArray(arraySize);
        int[] sortedArray = generateSortedArray(arraySize);
        int[] inverselySortedArray = generateInverselySortedArray(arraySize);

        System.out.println("Comparing sorting algorithms on different types of arrays...\n");

        compareSortingAlgorithms("Random Array", randomArray);
        compareSortingAlgorithms("Sorted Array", sortedArray);
        compareSortingAlgorithms("Inversely Sorted Array", inverselySortedArray);
    }

    private static void compareSortingAlgorithms(String arrayType, int[] array) {
        System.out.println("Array Type: " + arrayType);

        System.out.println("Linear (Insertion) Sort:");
        runSortAlgorithm(array.clone(), SortingComparison::insertionSort);

        System.out.println("Bubble Sort:");
        runSortAlgorithm(array.clone(), SortingComparison::bubbleSort);

        System.out.println("Quick Sort:");
        runSortAlgorithm(array.clone(), SortingComparison::quickSort);

        System.out.println();
    }

    private static void runSortAlgorithm(int[] array, SortAlgorithm algorithm) {
        long startTime = System.nanoTime();
        SortingStats stats = algorithm.sort(array);
        long endTime = System.nanoTime();

        System.out.println("Runtime: " + (endTime - startTime) / 1e6 + " ms");
        System.out.println("Comparisons: " + stats.comparisons);
        System.out.println("Interchanges: " + stats.interchanges);
        System.out.println();
    }

    interface SortAlgorithm {
        SortingStats sort(int[] array);
    }

    static class SortingStats {
        int comparisons;
        int interchanges;

        SortingStats(int comparisons, int interchanges) {
            this.comparisons = comparisons;
            this.interchanges = interchanges;
        }
    }

    // Insertion Sort
    public static SortingStats insertionSort(int[] array) {
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
            }
            array[j + 1] = key;
            interchanges++;
        }
        return new SortingStats(comparisons, interchanges);
    }

    // Bubble Sort
    public static SortingStats bubbleSort(int[] array) {
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
                }
            }
            if (!swapped) break;
        }
        return new SortingStats(comparisons, interchanges);
    }

    // Quick Sort
    public static SortingStats quickSort(int[] array) {
        return quickSort(array, 0, array.length - 1, new SortingStats(0, 0));
    }

    private static SortingStats quickSort(int[] array, int low, int high, SortingStats stats) {
        if (low < high) {
            int pi = partition(array, low, high, stats);
            quickSort(array, low, pi - 1, stats);
            quickSort(array, pi + 1, high, stats);
        }
        return stats;
    }

    private static int partition(int[] array, int low, int high, SortingStats stats) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            stats.comparisons++;
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
                stats.interchanges++;
            }
        }
        swap(array, i + 1, high);
        stats.interchanges++;
        return i + 1;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Array generation methods
    private static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(10000);
        }
        return array;
    }

    private static int[] generateSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    private static int[] generateInverselySortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }
}

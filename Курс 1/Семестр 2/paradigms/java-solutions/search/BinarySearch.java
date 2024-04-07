package search;

/* Перед командой указаны условия, которые необходимы для выполнения команды,
 *  и те, которые меняются после её выполнения. После команды указаны условия,
 *  которые появились или изменились после выполнения команды. При этом понимается,
 *  что неуказанные команды не используются и не изменяются */

public class BinarySearch {

    // Pred:
    // length = len(array) - 1
    // array[0] >= array[1] >= ... >= array[length]
    // len(array) >= 2
    // array[0] > number
    // array[len(array) - 1] <= number
    public static int iterativeSearch(int[] array, int number) {

        // array[0] > number
        int left = 0;
        // 0 <= 0 = left < len(array)
        // array[left] > number

        // left = 0
        // len(array) >= 2
        // array[len(array) - 1] <= number
        int right = array.length - 1;
        // 0 < 1 <= right = len(array) - 1 < len(array)
        // array[right] <= number
        // right >= 1
        // left = 0 < 1 = right   ->   right - left >= 1

        // Inv:
        // 0 <= left < right < len(array)
        // array[left] > number
        // array[right] <= number
        while (right - left > 1) {
            // right - left >= 2

            // right - left >= 2
            int median = left + (right - left) / 2;
            // left < median < right =>
            // => median - left < right - left && right - median < right - left

            // 0 <= left < median < right < len(array)
            // array[left] > number
            // array[right] <= number
            if (array[median] > number) {
                // array[median] > number

                // 0 <= left < median < right < len(array)
                // array[median] > number
                // array[right] <= number
                left = median;
                // 0 <= left < left' < right' = right < len(array)
                // array[left'] > number
                // array[right'] <= number
            } else {
                // array[median] <= number

                // 0 <= left < median < right < len(array)
                // array[left] > number
                // array[median] <= number
                right = median;
                // 0 <= left = left' < right' < right < len(array)
                // array[left'] > number
                // array[right'] <= number
            }
            // 0 <= left' < right' < len(array)
            // array[left'] > number
            // array[right'] <= number
            // right' - left' < right - left   -   гарантировано уменьшение отрезка, цикл не уйдет в бесконечность
        }
        // left = right - 1   ->   right = left + 1
        // 0 <= left < left + 1 = right < len(array)
        // array[left] > number
        // array[left + 1] = array[right] <= number

        // 0 <= left < left + 1 < len(array)
        // array[left] > number
        // array[left + 1] = array[right] <= number
        // искомый индекс в массиве array равен left + 1, но в силу смещенной на 1 индексации необходимо вернуть left
        return left; // = result

        // в случае когда number = Integer.MAX_VALUE или для пустого массива: result = 0 (это легко проверить отдельно)
    }
    // Post:
    // 0 <= result < result + 1 < len(array)
    // array[result] > number
    // array[result + 1] <= number




    // Pred:
    // array[0] >= array[1] >= ... >= array[length]
    // len(array) >= 2
    // 0 <= left < right < len(array)
    // array[left] > number
    // array[right] <= number
    public static int recursiveSearch(int[] array, int number, int left, int right) {

        // array[0] >= array[1] >= ... >= array[length]
        // len(array) >= 2
        // 0 <= left < right < len(array)
        // array[left] > number
        // array[right] <= number
        // right - left >= 1
        if (right - left == 1) {
            // right - left = 1

            // right = left + 1
            // 0 <= left < left + 1 < len(array)
            // array[left] > number
            // array[left + 1] <= number
            // искомый индекс в массиве array равен right, но в силу смещенной на 1 индексации необходимо вернуть right - 1 = left
            return left; // = result
            // 0 <= result < result + 1 < len(array)
            // array[result] > number
            // array[result + 1] <= number

            // в случае когда number = Integer.MAX_VALUE или для пустого массива: result = 0 (это легко проверить отдельно)
        }
        // right - left >= 2

        // right - left >= 2
        int median = left + (right - left) / 2;
        // left < median < right =>
        // => median - left < right - left && right - median < right - left

        // left < median < right
        // array[0] >= array[1] >= ... >= array[length]
        // len(array) >= 2
        // 0 <= left < right < len(array)
        // array[left] > number
        // array[right] <= number
        if (array[median] > number) {
            // array[median] > number

            // array[0] >= array[1] >= ... >= array[length]
            // len(array) >= 2
            // 0 <= left < median < right < len(array)
            // array[median] > number
            // array[right] <= number
            return recursiveSearch(array, number, median, right); // = result
            // 0 <= result < result + 1 < len(array)
            // array[result] > number
            // array[result + 1] <= number
        } else {
            // array[median] <= number

            // array[0] >= array[1] >= ... >= array[length]
            // len(array) >= 2
            // 0 <= left < median < right < len(array)
            // array[left] > number
            // array[median] <= number
            return recursiveSearch(array, number, left, median); // = result
            // 0 <= result < result + 1 < len(array)
            // array[result] > number
            // array[result + 1] <= number
        }

    }
    // Post:
    // 0 <= result < result + 1 < len(array)
    // array[left] > number
    // array[left + 1] <= number




    // Pred:
    // args contains integer strings
    // args contains at least one string
    // int(args1[1]) >= ... >= int(args[len(args) - 1]   -   элементы поступающего массива
    public static void main(String[] args) {

        // args contains integer strings
        // args contains at least one string
        int number = Integer.parseInt(args[0]);
        // number = int(args[0])   -   x по условию задачи

        // args contains at least one string
        int length = args.length;
        // length = len(args)
        // length >= 1

        // length + 1 >= 2
        int[] array = new int[length + 1];
        // len(array) = length + 1
        // len(array) >= 2
        // array = [0, 0, ..., 0]

        // array = [0, 0, ..., 0]
        // len(array) >= 2
        array[0] = Integer.MAX_VALUE;
        // array = [2147483647, 0, ..., 0]

        // -
        int i = 1;
        // i = 1

        // Inv:
        // i >= 1
        while (i < length) {
            // i < length
            // i >= 1

            // 0 < 1 <= i < length < len(array)
            // args contains integer strings
            // array = [2147483647, int(args[1], ..., int(args[i - 1]), 0, ..., 0]
            array[i] = Integer.parseInt(args[i]);
            // array[i] = int(args[i])
            // array = [2147483647, int(args[1], ..., int(args[i]), 0, ..., 0]

            // i' = i
            // i' >= 1
            i++;
            // i = i' + 1
            // i > i' >= 1
        }
        // i >= length >= 1
        // array = [2147483647, int(args[1], ..., int(args[length - 1]), 0]

        // 1 <= length < len(array)
        // array = [2147483647, int(args[1], ..., int(args[length - 1]), 0]
        // int(args1[1]) >= ... >= int(args[len(args) - 1]
        array[length] = Integer.MIN_VALUE;
        // array = [2147483647, int(args[1], ..., int(args[length - 1]), -2147483648]
        // array[0] >= array[1] >= ... >= array[length]

        // array[0] >= array[1] >= ... >= array[length]
        // len(array) >= 2
        // array[0] < number
        // array[len(array) - 1] >= number
        /* int ans = iterativeSearch(array, number); */
        // 0 <= ans < ans + 1 < len(array)
        // array[ans] > number &&
        // array[ans + 1] <= number
        // =>
        // 0 <= ans < length
        // (ans >= 1 && int(args[ans]) > number || ans = 0)
        // int(args[ans + 1]) <= number

        // array[0] >= array[1] >= ... >= array[length]
        // len(array) >= 2
        // 0 <= 0 < length < len(array)
        // array[0] > number
        // array[length] <= number
        int ans = recursiveSearch(array, number, 0, length);
        // 0 <= ans < ans + 1 < len(array)
        // array[ans] > number
        // array[ans + 1] <= number
        // =>
        // 0 <= ans < length
        // (ans >= 1 && int(args[ans]) > number || ans = 0)
        // int(args[ans + 1]) <= number

        System.out.println(ans);

    }
    // Post:
    // 0 <= ans < length
    // (ans >= 1 && int(args[ans]) > number || ans = 0)
    // int(args[ans + 1]) <= number

    // Следующие случаи рассматриваются отдельно:
    // - если number = Integer.MAX_VALUE (будет возвращен 0)
    // - если length = 1 (т.е. массив, в котором ищем, пустой, тогда будет возвращен 0)
    // - если все элементы массива больше number (будет возвращена длина массива)
}

package search;

/* Перед командой указаны условия, которые необходимы для выполнения команды,
 *  и те, которые меняются после её выполнения. После команды указаны условия,
 *  которые появились или изменились после выполнения команды. При этом понимается,
 *  что неуказанные команды не используются и не изменяются */

public class BinarySearchClosestA {

    // Pred:
    // array[0] <= array[1] <= ... <= array[len(array) - 1]
    // len(array) >= 3
    // array[0] < number
    // array[len(array) - 1] >= number
    public static int iterativeSearch(int[] array, int number) {

        // array[0] < number
        int left = 0;
        // 0 <= 0 = left < len(array)
        // array[left] < number

        // left = 0
        // len(array) >= 3
        // array[len(array) - 1] >= number
        int right = array.length - 1;
        // 0 < 2 <= right = len(array) - 1 < len(array)
        // array[right] >= number
        // right >= 2
        // left = 0 < 2 <= right   ->   right - left >= 2

        // Inv:
        // 0 <= left < right < len(array)
        // array[left] < number
        // array[right] >= number
        while (right - left > 1) {
            // right - left >= 2

            // right - left >= 2
            int median = left + (right - left) / 2;
            // left < median < right   ->
            // ->   median - left < right - left && right - median < right - left

            // 0 <= left < median < right < len(array)
            // array[left] < number
            // array[right] >= number
            if (array[median] < number) {
                // array[median] < number

                // 0 <= left < median < right < len(array)
                // array[median] < number
                // array[right] >= number
                left = median;
                // 0 <= left < left' < right' = right < len(array)
                // array[left'] < number
                // array[right'] >= number
            } else {
                // array[median] >= number

                // 0 <= left < median < right < len(array)
                // array[left] < number
                // array[median] >= number
                right = median;
                // 0 <= left = left' < right' < right < len(array)
                // array[left'] < number
                // array[right'] >= number
            }
            // 0 <= left' < right' < len(array)
            // array[left'] < number
            // array[right'] >= number
            // right' - left' < right - left   -   гарантировано уменьшение отрезка, цикл не уйдет в бесконечность
        }
        // right - left = 1   ->   right = left + 1
        // 0 <= left < left + 1 = right < len(array)
        // array[left] < number
        // array[left + 1] = array[right] >= number

        // array[0] <= array[1] <= ... <= array[len(array) - 1]
        // len(array) >= 3
        // 0 <= left <= len(array) - 2
        // array[left] < number
        // array[left + 1] >= number
        return returnValue(array, number, left); // = result
        // |result - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[len(array) - 2] - number|)
    }
    // Post:
    // |result - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[len(array) - 2] - number|)




    // Pred:
    // array[0] <= array[1] <= ... <= array[len(array) - 1]
    // len(array) >= 3
    // 0 <= left < right < len(array)
    // array[left] < number
    // array[right] >= number
    public static int recursiveSearch(int[] array, int number, int left, int right) {

        // array[0] <= array[1] <= ... <= array[len(array) - 1]
        // len(array) >= 3
        // 0 <= left < right < len(array)
        // array[left] < number
        // array[right] >= number
        if (right - left == 1) {
            // right - left = 1   ->   right = left + 1

            // array[0] <= array[1] <= ... <= array[len(array) - 1]
            // len(array) >= 3
            // 0 <= left <= len(array) - 2
            // array[left] < number
            // array[right] = array[left + 1] >= number
            return returnValue(array, number, left); // = result
            // |result - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[len(array) - 2] - number|)
        }
        // right - left >= 2

        // right - left >= 2
        int median = left + (right - left) / 2;
        // left < median < right   ->
        // ->   median - left < right - left && right - median < right - left

        // array[0] <= array[1] <= ... <= array[len(array) - 1]
        // len(array) >= 3
        // 0 <= left < right < len(array)
        // array[left] < number
        // array[right] >= number
        if (array[median] < number) {
            // array[median] < number

            // array[0] <= array[1] <= ... <= array[len(array) - 1]
            // len(array) >= 3
            // 0 <= left < median < right < len(array)
            // array[median] < number
            // array[right] >= number
            return recursiveSearch(array, number, median, right); // = result
            // |result - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[len(array) - 2] - number|)
        } else {
            // array[median] >= number

            // array[0] <= array[1] <= ... <= array[len(array) - 1]
            // len(array) >= 3
            // 0 <= left < median < right < len(array)
            // array[left] < number
            // array[median] >= number
            return recursiveSearch(array, number, left, median); // = result
            // |result - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[len(array) - 2] - number|)
        }

    }
    // Post:
    // |result - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[len(array) - 2] - number|)



    
    // Pred:
    // array[0] <= array[1] <= ... <= array[len(array) - 1]
    // len(array) >= 3
    // 0 <= left <= len(array) - 2
    // array[left] < number
    // array[left + 1] >= number
    public static int returnValue(int[] array, int number, int left) {

        // array[0] <= array[1] <= ... <= array[len(array) - 1]
        // len(array) >= 3
        // 0 <= left <= len(array) - 2
        // array[left] < number
        // array[left + 1] >= number
        if (left == 0) {
            // left = 0

            // 0 <= array[1] - number <= array[2] - number <= ... <= array[len(array) - 2] - number
            return array[1]; // = result
        } else if (left == array.length - 2) {
            // left = len(array) - 2

            // 0 <= number - array[left] <= number - array[left - 1] <= ... <= number - array[1]
            return array[left]; // = result
        } else if (array[left + 1] - number >= number - array[left]) { // >= т.к. приоритет у того что левее
            // 0 < left < len(array) - 2
            // number - array[left] <= array[left + 1] - number

            // 0 <= number - array[left] <= number - array[left - 1] <= ... <= number - array[1]
            // 0 <= array[left + 1] - number <= array[left + 2] - number <= ... <= array[len(array) - 2] - number
            // number - array[left] <= array[left + 1] - number
            return array[left]; // = result
        } else {
            // 0 < left < len(array) - 2
            // number - array[left] > array[left + 1] - number

            // 0 <= number - array[left] <= number - array[left - 1] <= ... <= number - array[1]
            // 0 <= array[left + 1] - number <= array[left + 2] - number <= ... <= array[len(array) - 2] - number
            // number - array[left] > array[left + 1] - number
            return array[left + 1]; // = result
        }
        // |result - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[len(array) - 2] - number|)
    }
    // Post:
    // |result - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[len(array) - 2] - number|)




    // Pred:
    // args contains integer strings
    // args contains at least two strings
    // int(args[1]) <= int(args[2]) <= ... <= int(args[len(args) - 1])   -   элементы поступающего массива
    public static void main(String[] args) {

        // args contains integer strings
        // args contains at least two strings
        int number = Integer.parseInt(args[0]);
        // number = int(args[0])   -   x по условию задачи

        // args contains at least two strings
        int length = args.length;
        // length = len(args)
        // length >= 2

        // length >= 2
        int[] array = new int[length + 1];
        // len(array) = length + 1
        // len(array) >= 3
        // array = [0, 0, ..., 0]

        // array = [0, 0, ..., 0]
        // len(array) >= 3
        array[0] = Integer.MIN_VALUE;
        // array = [-2147483648, 0, ..., 0]

        // -
        int i = 1;
        // i = 1

        // -
        int sum = 0;
        // sum = 0

        // Inv:
        // i >= 1
        while (i < length) {
            // i < length

            // 0 < 1 <= i < length < len(array)
            // args contains integer strings
            // array = [-2147483648, int(args[1], ..., int(args[i - 1]), 0, ..., 0]
            array[i] = Integer.parseInt(args[i]);
            // array[i] = int(args[i])
            // array = [-2147483648, int(args[1], ..., int(args[i]), 0, ..., 0]

            // sum' = sum
            sum += array[i];
            // sum = sum' + array[i]

            // i' = i &&
            // i >= 1 ->
            // -> i' >= 1
            i++;
            // i = i' + 1
            // i > i' >= 1
        }
        // i = length >= 2 > 1
        // array = [-2147483648, int(args[1], ..., int(args[length - 1]), 0]
        // sum = array[1] + ... + array[length - 1]

        // 1 <= length < len(array)
        // array = [-2147483648, int(args[1], ..., int(args[length - 1]), 0]
        // int(args1[1]) <= ... <= int(args[len(args) - 1]
        array[length] = Integer.MAX_VALUE;
        // array = [-2147483648, int(args[1], ..., int(args[length - 1]), 2147483647]
        // array[0] <= array[1] <= ... <= array[len(array) - 1]

        // -
        int answer;
        // answer is declared

        // sum = array[1] + ... + array[length - 1]
        // array[0] <= array[1] <= ... <= array[len(array) - 1]
        // len(array) >= 3
        // array[0] < number
        // array[len(array) - 1] >= number
        // length - 0 >= 2
        if (sum % 2 == 1) {
            // sum % 2 = 1

            // array[0] <= array[1] <= ... <= array[len(array) - 1]
            // len(array) >= 3
            // array[0] < number
            // array[len(array) - 1] >= number
            answer = (iterativeSearch(array, number));
            // |answer - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[length - 1] - number|)
        } else {
            // sum % 2 = 0

            // array[0] <= array[1] <= ... <= array[len(array) - 1]
            // len(array) >= 3
            // array[0] < number
            // number <= array[length]
            // length - 0 >= 2 >= 1
            answer = recursiveSearch(array, number, 0, length);
            // |answer - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[length - 1] - number|)
        }
        // |answer - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[length - 1] - number|)

        System.out.println(answer);

    }
    // Post:
    // |answer - number| = min(|array[1] - number|, |array[2] - number|, ..., |array[length - 1] - number|)

    // Следующие случаи рассматриваются отдельно:
    // - если number = Integer.MIN_VALUE (будет возвращен int(args[1]))
}

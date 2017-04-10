/**
 * 
 */
package com.wasu.ptyw.galaxy.common.util;

import java.util.Arrays;
import java.util.Random;

/**
 * http://www.importnew.com/8445.html#comment-13445
 * http://java.dzone.com/articles/algorithm-week-quicksort-three
 * @author wenguang
 * @date 2014年1月16日
 */
public class SortUtil {
	public static void sort(int[] input) {
		// sortBySualPivot(input, 0, input.length - 1);
		//quickSort(input, 0, input.length - 1);
		quickSort3Way(input, 0, input.length - 1);
	}

	/**
	 * 双基准快速排序
	 */
	private static void sortBySualPivot(int[] input, int lowIndex, int highIndex) {
		if (highIndex <= lowIndex)
			return;

		int pivot1 = input[lowIndex];
		int pivot2 = input[highIndex];

		if (pivot1 > pivot2) {
			swap(input, lowIndex, highIndex);
			pivot1 = input[lowIndex];
			pivot2 = input[highIndex];
		} else if (pivot1 == pivot2) {
			int temIndex = lowIndex;
			while (pivot1 == pivot2 && temIndex < highIndex) {
				temIndex++;
				pivot1 = input[temIndex];
			}
		}

		int i = lowIndex + 1;
		int lt = lowIndex + 1;
		int gt = highIndex - 1;

		while (i <= gt) {
			if (input[i] < pivot1) {
				swap(input, i++, lt++);
			} else if (pivot2 < input[i]) {
				swap(input, i, gt--);
			} else {
				i++;
			}
		}

		swap(input, lowIndex, --lt);
		swap(input, highIndex, ++gt);

		sortBySualPivot(input, lowIndex, lt - 1);
		sortBySualPivot(input, lt + 1, gt - 1);
		sortBySualPivot(input, gt + 1, highIndex);

	}

	/**
	 * 三路（Three-way）快速排序
	 */
	private static void quickSort3Way(int[] input, int lowIndex, int highIndex) {
		if (highIndex <= lowIndex)
			return;

		int lt = lowIndex;
		int gt = highIndex;
		int i = lowIndex + 1;

		int pivotIndex = lowIndex;
		int pivotValue = input[pivotIndex];

		while (i <= gt) {
			if (less(input[i], pivotValue)) {
				swap(input, i++, lt++);
			} else if (less(pivotValue, input[i])) {
				swap(input, i, gt--);
			} else {
				i++;
			}

		}
		quickSort3Way(input, lowIndex, lt - 1);
		quickSort3Way(input, gt + 1, highIndex);
	}

	/**
	 * 快速排序
	 */
	private static void quickSort(int[] input, int lowIndex, int highIndex) {
		if (highIndex <= lowIndex) {
			return;
		}

		int partIndex = partition(input, lowIndex, highIndex);

		quickSort(input, lowIndex, partIndex - 1);
		quickSort(input, partIndex + 1, highIndex);
	}

	private static int partition(int[] input, int lowIndex, int highIndex) {
		int i = lowIndex;
		int pivotIndex = lowIndex;
		int j = highIndex + 1;
		while (true) {
			while (less(input[++i], input[pivotIndex])) {
				if (i == highIndex)
					break;
			}
			while (less(input[pivotIndex], input[--j])) {
				if (j == lowIndex)
					break;
			}
			if (i >= j)
				break;
			swap(input, i, j);
		}
		swap(input, pivotIndex, j);
		return j;
	}

	/**
	 * Swaps x[a] with x[b].
	 */
	public static void swap(int[] x, int a, int b) {
		if ((x[a] == 98 && x[b] == 92) || (x[a] == 92 && x[b] == 98)) {
			int i = 1;
			i++;
		}
		if (a == b)
			return;
		int t = x[a];
		x[a] = x[b];
		x[b] = t;
	}

	public static boolean less(int a, int b) {
		return a < b;
	}

	public static void main(String[] args) {
		int length = 50;
		int[] input = { 3, 4, 2, 1, 3 };
		input = new int[length];
		Random r = new Random();
		for (int i = 0; i < length; i++) {
			input[i] = r.nextInt(100);
		}

		String s = "68 41 14 42 35 51 02 88 68 26 23 98 09 84 06 30 92 12 88 44 21 15 64 22 36 24 87 33 23 64 18 72 88 32 91 04 31 53 85 55 88 02 10 82 12 45 06 98 23 27";
		String[] sArr = s.split(" ");
		for (int i = 0; i < sArr.length; i++) {
			input[i] = Integer.parseInt(sArr[i]);
		}

		System.out.println(Arrays.toString(input));
		SortUtil.sort(input);
		System.out.println(Arrays.toString(input));
		for (int i = 0; i < length; i++) {
			if (i < length - 2 && input[i] > input[i + 1]) {
				System.out.println("sort error");
				break;
			}
		}
	}
}

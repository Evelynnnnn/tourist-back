package com.shilei.tourist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        b();
    }

    static void b(){
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int[] arr = new int[a];
        for (int i = 0; i < a; i++) {
            arr[i] = scanner.nextInt();
        }
        for (int i = 0; i < a; i++) {
            if (arr[i] % 4 == 1){
                System.out.println(1 + " " + arr[i]);
            }else  if (arr[i] % 4 == 2){
                System.out.println(1 + " " + (arr[i] - 1));
            }else  if (arr[i] % 4 == 3){
                System.out.println(0 + " " + (arr[i] - 1));
            }else{
                System.out.println(0 + " " + arr[i]);
            }
        }
    }
}

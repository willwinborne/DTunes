package net.winborne.net.winborne.maven.dtunes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class RunnableProgressDialogUpdater implements Runnable {

	public void run() {
		System.out.println("RPDU runnable created");
		Scanner sc = new Scanner(new File("log.txt"));
        while(sc.hasNextLine()){
            System.out.println(sc.nextLine());
        }
		String line = ;
		try {
			line = reader.readLine();
			System.out.println(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

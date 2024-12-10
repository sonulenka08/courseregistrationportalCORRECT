package com.example.courseregistration.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileService<T> {
	private String name = Object.class.getSimpleName();
	
	public FileService(String name) {
		this.name = name;
	} 
	
	public List<String> readData(String fileLoaction) {
		List<String> csvStrings = new ArrayList<>();
		try {
			Scanner csvData = new Scanner(new BufferedReader(new FileReader(fileLoaction)));
			while (csvData.hasNextLine()) {
				csvStrings.add(csvData.nextLine());
			}
			csvData.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return csvStrings;
	}
}

package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	Scanner input = new Scanner(System.in);
	private DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();

		app.launch();
	}

	private void test() {
		Actor actor = db.findActorById(5);
		System.out.println(actor);
	}

	private void launch() {

		startUserInterface(input);

		input.close();
		// terminate program when user selects "Quit"

	}

	private void startUserInterface(Scanner input) {
//menu options

		while (true) {
			System.out.println("");
			System.out.println("1. Look up a film by its id");
			System.out.println("2. Look up a film by a search keyword");
			System.out.println("3. Exit application");
			int option = input.nextInt();
			switch (option) {

			case 1:
				findFilmById();
				break;
			case 2:
				findFilmByKeyword();
				break;
			case 3:
				System.out.println("Goodbye!");
				System.exit(option);
			}
		}
	}

	private void findFilmByKeyword() {
		System.out.println("Please enter a keyword to search a film");
		// TODO Auto-generated method stub

	}

	private void findFilmById() {
		System.out.println("Please enter the film id number");
		int option = input.nextInt();
		Film film = db.findFilmById(option);
		
		if (film != null) {
			System.out.println(film);
		} else {

			System.out.println("No movie was found under this id. Please try again.");
			// TODO Auto-generated method stub
		}

	}

}

package com.tvtoner.schoolapp;

import com.tvtoner.schoolapp.dao.StudentRepository;
import com.tvtoner.schoolapp.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SchoolappApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolappApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StudentRepository studentRepo) {

		return runner -> {

			getAllStudents(studentRepo);
		};
	}

	private void getAllStudents(StudentRepository studentRepo) {
		List<Student> students = studentRepo.findAll();

		System.out.println(students);
	}

}

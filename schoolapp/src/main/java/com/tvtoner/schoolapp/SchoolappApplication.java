package com.tvtoner.schoolapp;

import com.tvtoner.schoolapp.dao.StudentRepository;
import com.tvtoner.schoolapp.entity.Course;
import com.tvtoner.schoolapp.entity.Student;
import com.tvtoner.schoolapp.service.AdminService;
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
	public CommandLineRunner commandLineRunner(AdminService adminService ) {

		return runner -> {

//			getAllStudents(studentRepo);

			// addCourse(adminService);
		};
	}

	private void addCourse(AdminService adminService) {

		//create course
		adminService.addCourse("Java");
		adminService.addCourse("Python");
		adminService.addCourse("SQL");

		System.out.println("Courses Added");
		System.out.println(adminService.getCourseById(1).getInstructor());
	}

	private void getAllStudents(StudentRepository studentRepo) {
		List<Student> students = studentRepo.findAll();

		System.out.println(students);
	}

}

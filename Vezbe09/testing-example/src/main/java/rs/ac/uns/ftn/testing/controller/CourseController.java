package rs.ac.uns.ftn.testing.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.testing.dto.CourseDTO;
import rs.ac.uns.ftn.testing.dto.ExamDTO;
import rs.ac.uns.ftn.testing.dto.StudentDTO;
import rs.ac.uns.ftn.testing.model.Course;
import rs.ac.uns.ftn.testing.model.Exam;
import rs.ac.uns.ftn.testing.service.CourseService;

@RestController
@RequestMapping(value="api/courses")
public class CourseController {
	@Autowired
	private CourseService courseService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CourseDTO>> getCourses() {
		
		List<Course> courses = courseService.findAll();
		
		//convert courses to DTOs
		List<CourseDTO> coursesDTO = new ArrayList<CourseDTO>();
		for (Course s : courses) {
			coursesDTO.add(new CourseDTO(s));
		}
		
		return new ResponseEntity<List<CourseDTO>>(coursesDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id){
		
		Course course = courseService.findOne(id);
		
		// course must exist
		if(course == null){
			return new ResponseEntity<CourseDTO>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<CourseDTO>(new CourseDTO(course), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<CourseDTO> saveCourse(@RequestBody CourseDTO courseDTO){
		
		Course course = new Course();
		course.setName(courseDTO.getName());
	
		course = courseService.save(course);
		return new ResponseEntity<CourseDTO>(new CourseDTO(course), HttpStatus.CREATED);	
	}
	
	@RequestMapping(method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<CourseDTO> updateCourse(@RequestBody CourseDTO courseDTO){
		
		//a course must exist
		Course course = courseService.findOne(courseDTO.getId()); 
		
		if (course == null) {
			return new ResponseEntity<CourseDTO>(HttpStatus.BAD_REQUEST);
		}
		
		course.setName(courseDTO.getName());
	
		course = courseService.save(course);
		return new ResponseEntity<CourseDTO>(new CourseDTO(course), HttpStatus.OK);	
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteCourse(@PathVariable Long id){
		
		Course course = courseService.findOne(id);
		
		if (course != null){
			courseService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {		
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{courseId}/exams", method = RequestMethod.GET)
	public ResponseEntity<List<ExamDTO>> getStudentExams(
			@PathVariable Long courseId) {
		
		Course course = courseService.findOne(courseId);
		
		Set<Exam> exams = course.getExams();
		List<ExamDTO> examsDTO = new ArrayList<ExamDTO>();
		
		for (Exam e: exams) {
			ExamDTO examDTO = new ExamDTO();
			examDTO.setId(e.getId());
			examDTO.setGrade(e.getGrade());
			examDTO.setDate(e.getDate());
			examDTO.setStudent(new StudentDTO(e.getStudent()));
		
			examsDTO.add(examDTO);
		}
		
		return new ResponseEntity<List<ExamDTO>>(examsDTO, HttpStatus.OK);
	}
}

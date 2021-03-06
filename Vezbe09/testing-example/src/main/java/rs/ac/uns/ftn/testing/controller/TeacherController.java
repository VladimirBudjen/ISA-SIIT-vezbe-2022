package rs.ac.uns.ftn.testing.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.testing.dto.CourseDTO;
import rs.ac.uns.ftn.testing.dto.TeacherDTO;
import rs.ac.uns.ftn.testing.model.Course;
import rs.ac.uns.ftn.testing.model.Teacher;
import rs.ac.uns.ftn.testing.service.TeacherService;

@RestController
@RequestMapping(value="api/teachers")
public class TeacherController {
	@Autowired
	private TeacherService teacherService;
	
	@RequestMapping(value="/all", method = RequestMethod.GET)
	public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
		
		List<Teacher> teachers = teacherService.findAll();
		
		//convert teachers to DTOs
		List<TeacherDTO> teachersDTO = new ArrayList<TeacherDTO>();
		for (Teacher s : teachers) {
			teachersDTO.add(new TeacherDTO(s));
		}
		
		return new ResponseEntity<List<TeacherDTO>>(teachersDTO, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<TeacherDTO>> getTeachersPage(Pageable page) {
		
		Page<Teacher> teachers = teacherService.findAll(page);
		
		//convert teachers to DTOs
		List<TeacherDTO> teachersDTO = new ArrayList<TeacherDTO>();
		for (Teacher s : teachers) {
			teachersDTO.add(new TeacherDTO(s));
		}
		
		return new ResponseEntity<List<TeacherDTO>>(teachersDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<TeacherDTO> getTeacher(@PathVariable Long id){
		
		Teacher teacher = teacherService.findOne(id);
		
		if(teacher == null){
			return new ResponseEntity<TeacherDTO>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<TeacherDTO>(new TeacherDTO(teacher), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<TeacherDTO> saveTeacher(@RequestBody TeacherDTO teacherDTO){
		
		Teacher teacher = new Teacher();
		teacher.setFirstName(teacherDTO.getFirstName());
		teacher.setLastName(teacherDTO.getLastName());
		
		teacher = teacherService.save(teacher);
		return new ResponseEntity<TeacherDTO>(new TeacherDTO(teacher), HttpStatus.CREATED);	
	}
	
	@RequestMapping(method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<TeacherDTO> updateTeacher(@RequestBody TeacherDTO teacherDTO){
		
		//a teacher must exist
		Teacher teacher = teacherService.findOne(teacherDTO.getId()); 
		
		if (teacher == null) {
			return new ResponseEntity<TeacherDTO>(HttpStatus.BAD_REQUEST);
		}
		
		teacher.setFirstName(teacherDTO.getFirstName());
		teacher.setLastName(teacherDTO.getLastName());
		
		teacher = teacherService.save(teacher);
		return new ResponseEntity<TeacherDTO>(new TeacherDTO(teacher), HttpStatus.OK);	
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteTeacher(@PathVariable Long id){
		
		Teacher teacher = teacherService.findOne(id);
		
		if (teacher != null){
			teacherService.remove(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {		
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{teacherId}/courses", method = RequestMethod.GET)
	public ResponseEntity<List<CourseDTO>> getTeacherCourses(
			@PathVariable Long teacherId) {
		
		Teacher teacher = teacherService.findOne(teacherId);
		
		Set<Course> courses = teacher.getCourses();
		List<CourseDTO> coursesDTO = new ArrayList<CourseDTO>();
		
		for (Course c: courses) {
			coursesDTO.add(new CourseDTO(c));
		}
		return new ResponseEntity<List<CourseDTO>>(coursesDTO, HttpStatus.OK);
	}
}

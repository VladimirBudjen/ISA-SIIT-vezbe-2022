package rs.ac.uns.ftn.informatika.rest.service;

import java.util.Collection;

import rs.ac.uns.ftn.informatika.rest.domain.Greeting;

public interface GreetingService {

	Collection<Greeting> findAll();

	Greeting findOne(Long id);

	Greeting create(Greeting greeting) throws Exception;

	Greeting update(Greeting greeting) throws Exception;

	void delete(Long id);
	
}
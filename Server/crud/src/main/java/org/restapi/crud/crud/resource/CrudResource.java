package org.restapi.crud.crud.resource;

import org.restapi.crud.crud.model.CrudModel;
import org.restapi.crud.crud.service.CrudService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Consumes;

import jakarta.ws.rs.DELETE;



@Path("/crud")
public class CrudResource {
	private CrudService service = new CrudService();
	
	@GET
	@Path("/words/{word}")
	@Produces(MediaType.APPLICATION_JSON)
	public CrudModel getword(@PathParam("word") String word) {
		return service.getEntries(word);
	}
	
	@POST
	@Path("/words")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addWord(CrudModel word){
		return service.addEntries(word);
	}
	
	@DELETE
	@Path("/words")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteWord(CrudModel word){
		return service.deleteEntries(word);
	}
	
	@PUT
	@Path("/words")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateWord(CrudModel word) {
		return service.updateEntries(word);
	}
	
}

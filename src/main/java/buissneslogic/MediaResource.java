/**
 * 
 */
package buissneslogic;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import models.*;

/**
 * @author rebec
 *
 */
@Path("/media")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class MediaResource {
	
	protected MediaResource() {
		
	}
	
	@POST
	@Path("/books")
	@Produces("application/json")
	public Response createBook(Book book) {
		return null;
	}
	
	@GET
	@Path("books") // qestion: "books" or "/books"
	public Response getBooks() {
		// todo 
		// get json object
		// de-serialize
		// pass to service
		return null;
	}
	
	
	@PUT
	@Path("books/{isbn}")
	public Response updateBook(Book book) {
		return null;
	}
	
	
}

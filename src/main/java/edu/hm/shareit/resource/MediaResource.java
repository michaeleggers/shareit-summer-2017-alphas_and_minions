
package edu.hm.shareit.resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.hm.shareit.model.Book;
import edu.hm.shareit.model.Disc;
import edu.hm.shareit.model.Medium;
import edu.hm.shareit.service.MediaService;
import edu.hm.shareit.service.MediaServiceImpl;
import edu.hm.shareit.service.MediaServiceResult;


/**
 * The interface that talks to the clients requests. 
 * 
 * @author Michael Eggers
 * @author Rebecca Brydon
 */
@Path("media")
public class MediaResource {
	
	/**
	 * Service does all the logic.
	 */
	private final MediaService service;
	
	/**
	 * MediaResource creates media resource.
	 */
	public MediaResource() {
		service = new MediaServiceImpl();
	}
	
	/**
	 * Getter for service.
	 * @return A mediaservice.
	 */
	public MediaService getService() {
		return service;
	}

	/**
	 * Creates a new book.
	 * @param book The book data in json.
	 * @return response Feedback to caller.
	 */
	@POST
	@Path("books")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createBook(Book book) {
		MediaServiceResult result = service.addBook(book);

		return Response
				.status(result.getErrorNum())
				.entity(errorMessageJSON(result).toString())
				.build();

	}
	
	/**
	 * Creates a new Disc.
	 * @param disc The disc data in json.
	 * @return response Feedback to caller.
	 */
	@POST
	@Path("discs")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createDisc(Disc disc) {
		MediaServiceResult result = service.addDisc(disc);
		
		return Response
				.status(result.getErrorNum())
				.entity(errorMessageJSON(result).toString())
				.build();
		
	}
	
	/**
	 * Gets all books.
	 * @return response All the books in json.
	 * @throws JsonProcessingException If object could not be serialized to json file.
	 */
	@GET
	@Path("books")
	@Produces("application/json")
	public Response getBooks() throws JsonProcessingException {
		MediaServiceResult result = MediaServiceResult.OK;
		Medium[] books = service.getBooks();
		//System.out.println("books array: " + Arrays.toString(books));
		ObjectMapper mapper = new ObjectMapper();
		
		if (result == MediaServiceResult.OK) {
			List<Medium> bookList = Arrays.stream(books).collect(Collectors.toList());
			String node = mapper.writeValueAsString(bookList);
			return Response
					.status(result.getErrorNum())
					.entity(node)
					.build();			
		} else {
			JSONObject obj = errorMessageJSON(result);
			return Response
					.status(result.getErrorNum())
					.entity(obj)
					.build();
		}
	}
	
	/**
	 * Gets all discs.
	 * @return All the discs in json.
	 * @throws JsonProcessingException If object could not be serialized to json file.
	 */
	@GET
	@Path("discs")
	@Produces("application/json")
	public Response getDiscs() throws JsonProcessingException {
		MediaServiceResult result = MediaServiceResult.OK;
		Medium[] discs = service.getDiscs();
		//System.out.println("books array: " + Arrays.toString(books));
		ObjectMapper mapper = new ObjectMapper();
		
		if (result == MediaServiceResult.OK) {
			List<Medium> bookList = Arrays.stream(discs).collect(Collectors.toList());
			String node = mapper.writeValueAsString(bookList);
			return Response
					.status(result.getErrorNum())
					.entity(node)
					.build();			
		} else {
			JSONObject obj = errorMessageJSON(result);
			return Response
					.status(result.getErrorNum())
					.entity(obj)
					.build();
		}
	}
	
	/**
	 * Gets a book with isbn.
	 * @param isbn ISBN of the wanted book.
	 * @return response Response to caller.
	 */
	@GET
	@Consumes({"text/plain", "application/json"})
	@Produces("application/json")
	@Path("books/{isbn}")
	public Response getBook(@PathParam("isbn") String isbn) {
		Optional<Medium> book = service.getBook(isbn);
		ObjectMapper mapper = new ObjectMapper();
		
		if (book.isPresent()) {
			ObjectNode node = mapper.valueToTree(book.get());
			return Response
					.status(MediaServiceResult.OK.getErrorNum())
					.entity(node)
					.build();
		} else {
			return Response
					.status(MediaServiceResult.NOT_FOUND.getErrorNum())
					.entity(errorMessageJSON(MediaServiceResult.NOT_FOUND).toString())
					.build();
		}
	}
	
	/**
	 * Gets a disc by its barcode.
	 * @param barcode Barcode of the wanted disc.
	 * @return Response to caller.
	 */
	@GET
	@Consumes({"text/plain", "application/json"})
	@Produces("application/json")
	@Path("discs/{barcode}")
	public Response getDisc(@PathParam("barcode") String barcode) {
		Optional<Medium> disc = service.getDisc(barcode);
		ObjectMapper mapper = new ObjectMapper();
		
		if (disc.isPresent()) {
			ObjectNode node = mapper.valueToTree(disc.get());
			return Response
					.status(MediaServiceResult.OK.getErrorNum())
					.entity(node)
					.build();
		} else {
			return Response
					.status(MediaServiceResult.NOT_FOUND.getErrorNum())
					.entity(errorMessageJSON(MediaServiceResult.NOT_FOUND).toString())
					.build();
		}
	}
	
	
	/**
	 * Updates book information.
	 * @param isbn ISBN of book to update.
	 * @param book New book data.
	 * @return response Response to caller.
	 */
	@PUT
	@Consumes("application/json")
	@Produces({"application/json", "text/plain"})
	@Path("books/{isbn}")
	public Response updateBook(@PathParam("isbn") String isbn, Book book) {
		MediaServiceResult result;
		if (!isbn.equals(book.getIsbn())) {
			result = MediaServiceResult.ISBN_NOT_EQUAL;
		}
		else {
			result = service.updateBook(book);
		}
		
		return Response.status(result.getErrorNum())
				.entity(errorMessageJSON(result).toString())
				.build();
	}
	
	/**
	 * Updates disc information.
	 * @param barcode Barcode of disc to update.
	 * @param disc New disc data.
	 * @return response Response to caller.
	 */
	@PUT
	@Consumes("application/json")
	@Produces({"application/json", "text/plain"})
	@Path("discs/{barcode}")
	public Response updateDisc(@PathParam("barcode") String barcode, Disc disc) {
		MediaServiceResult result;
		if (!barcode.equals(disc.getBarcode())) {
			result = MediaServiceResult.BARCODE_NOT_EQUAL;
		}
		else {
			result = service.updateDisc(disc);
		}
		
		return Response.status(result.getErrorNum())
				.entity(errorMessageJSON(result).toString())
				.build();
	}
	
	/**
	 * Creates a JSON obj with error message the form of simpleGeo (see 03-REST p.41).
	 * @param result The Response to convert.
	 * @return A JSON object holding the Response data.
	 */
	private JSONObject errorMessageJSON(MediaServiceResult result) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("code", result);
		jsonObj.put("detail", MediaServiceResult.getErrorMessage(result));
		return jsonObj;
	}
	
	
}

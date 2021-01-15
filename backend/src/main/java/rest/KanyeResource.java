package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import utils.HttpUtils;
import DTOs.KanyeDTO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marcus
 */
@Path("Kanye")
public class KanyeResource {
    

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private UriInfo context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getKanyeQuote() throws IOException {
        String chuck = HttpUtils.fetchData("https://api.kanye.rest\n");
        KanyeDTO kanyeDTO = GSON.fromJson(chuck, KanyeDTO.class);

        return GSON.toJson(kanyeDTO);
    }
}

package it.partec;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Student extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		long id = Long.parseLong(request.getParameter("id")); // leggo il parametro dalla query string
		JSONParser parser = new JSONParser();
		JSONObject result = null; 
		// leggo il file dalla cartella predisposta per contenere le risorse
		try(Reader file = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("liststudent.json"))) {
			Object obj = parser.parse(file);
			JSONArray listStudent = (JSONArray) obj;
			for(Object student: listStudent) {
				JSONObject studentJson = (JSONObject) student;
				if((Long) studentJson.get("id") == id) {
					result = studentJson;
					break;
				}
			}
		} catch(ParseException e) {
			e.printStackTrace();
		}
		if(result != null) {
			out.print(result.toJSONString());
		} else {
			// imposto l'http status 404
			response.setStatus(404);
			JSONObject error = new JSONObject();
			error.put("error", "not found");
			out.print(error.toJSONString());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONParser parser = new JSONParser();
		// lettura dei parametri dal form
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		long age = Long.parseLong(request.getParameter("age"));
		JSONArray listStudent = null;
		JSONObject newStudent = new JSONObject();
		long id = 0; 
		try(Reader file = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("liststudent.json"))) {
			Object obj = parser.parse(file);
			listStudent = (JSONArray) obj;
			for(Object student: listStudent) {
				JSONObject studentJson = (JSONObject) student;
				if((Long) studentJson.get("id") > id) {
					id = (Long) studentJson.get("id");
				}
			}
		} catch(ParseException e) {
			e.printStackTrace();
		}
		if(id != 0) {
			newStudent.put("id", id + 1);
			newStudent.put("name", name);
			newStudent.put("surname", surname);
			newStudent.put("age", age);
			listStudent.add(newStudent);
			// scrivo il file con il nuovo studente aggiunto
			try(Writer file = new PrintWriter(getClass().getClassLoader().getResource("liststudent.json").getFile())) {
				file.write(listStudent.toJSONString());
			} catch(Exception e) {
				e.printStackTrace();
				response.setStatus(503);
			}
			response.setStatus(201);
		} else {
			response.setStatus(503);
		}

	}


}

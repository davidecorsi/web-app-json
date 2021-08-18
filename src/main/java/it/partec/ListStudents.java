package it.partec;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ListStudents extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		JSONParser parser = new JSONParser();
		JSONArray listStudent = null;
		try(Reader file = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("liststudent.json"))) {
			Object obj = parser.parse(file);
			listStudent = (JSONArray) obj;
			out.print(listStudent.toJSONString());
		} catch(ParseException e) {
			e.printStackTrace();;
			response.setStatus(503);
		}
	}
}
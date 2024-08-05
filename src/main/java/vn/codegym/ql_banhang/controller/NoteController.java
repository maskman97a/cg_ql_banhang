package vn.codegym.ql_banhang.controller;

import vn.codegym.ql_banhang.service.NoteService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/note/*"})
public class NoteController extends HttpServlet {
    private NoteService noteService;

    @Override
    public void init() {
        this.noteService = new NoteService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getPathInfo() == null) {
            noteService.renderNotePage(req, resp);
            return;
        }
        switch (req.getPathInfo()) {
            case "/search":
                noteService.renderNotePage(req, resp);
                break;
            case "/create-note":
                noteService.renderCreateNoteForm(req, resp);
                break;
            case "/update-note":
                noteService.renderUpdateNoteForm(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        if (request.getPathInfo() == null) {

        }
        switch (request.getPathInfo()) {
            case "/search":
                break;
            case "/create-note":
                break;
            case "/update-note":
                break;
        }
    }
}

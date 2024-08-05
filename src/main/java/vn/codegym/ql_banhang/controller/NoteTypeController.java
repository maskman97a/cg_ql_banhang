package vn.codegym.ql_banhang.controller;

import vn.codegym.ql_banhang.service.NoteTypeService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/note-type/*"})
public class NoteTypeController extends HttpServlet {
    private NoteTypeService noteTypeService;

    @Override
    public void init() {
        this.noteTypeService = new NoteTypeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getPathInfo() == null) {
            noteTypeService.renderNoteTypePage(req, resp);
            return;
        }
        switch (req.getPathInfo()) {
            case "/search":
                noteTypeService.renderNoteTypePage(req, resp);
                break;
            case "/create":
                noteTypeService.renderCreateNoteTypeForm(req, resp);
                break;
            case "/update":
                noteTypeService.renderUpdateNoteForm(req, resp);
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
            case "/create-note-type":
                noteTypeService.createNew(request, response);
                break;
            case "/update-note":
                break;
        }
    }
}

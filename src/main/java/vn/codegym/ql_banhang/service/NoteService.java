package vn.codegym.ql_banhang.service;

import vn.codegym.ql_banhang.dto.BaseSearchDto;
import vn.codegym.ql_banhang.dto.Condition;
import vn.codegym.ql_banhang.entity.BaseData;
import vn.codegym.ql_banhang.entity.BaseEntity;
import vn.codegym.ql_banhang.entity.Note;
import vn.codegym.ql_banhang.entity.NoteType;
import vn.codegym.ql_banhang.model.ProductModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class NoteService extends BaseService {
    private ProductModel productModel;
    private NoteTypeService noteTypeService;

    public NoteService() {
        super(new ProductModel());
        this.noteTypeService = new NoteTypeService();
    }

    public void renderNotePage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("/views/note/note.jsp").forward(req, resp);
            doSearch(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void renderCreateNoteForm(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("/views/note/create-note.jsp").forward(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void renderUpdateNoteForm(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("/views/note/update-note.jsp").forward(req, resp);
            Note note = (Note) super.findById(Integer.valueOf(req.getParameter("id")));
            req.setAttribute("updatingNote", note);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doSearch(HttpServletRequest req, HttpServletResponse resp) {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        String noteType = req.getParameter("note-type");
        if (noteType != null && !noteType.isEmpty()) {
            Condition condition = new Condition();
            condition.setColumnName("type_id");
            condition.setOperator("=");
            condition.setValue(Integer.parseInt(noteType));
            baseSearchDto.getConditions().add(condition);
        }
        BaseData baseDataNoteType = noteTypeService.findAll();
        if (baseDataNoteType.getLstData() != null && !baseDataNoteType.getLstData().isEmpty()) {
            List<NoteType> noteTypeList = new ArrayList<>();
            for (BaseEntity baseEntity : baseDataNoteType.getLstData()) {
                noteTypeList.add((NoteType) baseEntity);
            }
            req.setAttribute("noteTypeList", noteTypeList);
        }

        BaseData baseData = super.doSearch(req, resp, baseSearchDto, Note.getSearchColumn());

        if (baseData != null && baseData.getLstData() != null && !baseData.getLstData().isEmpty()) {
            List<Note> lstData = new ArrayList<>();
            int index = 1;
            for (BaseEntity baseEntity : baseData.getLstData()) {
                Note note = (Note) baseEntity;
                note.setIndex(index++);
                lstData.add(note);
            }
            req.setAttribute("lstData", lstData);
        }
    }

}

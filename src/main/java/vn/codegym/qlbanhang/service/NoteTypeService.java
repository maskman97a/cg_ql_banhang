package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.entity.BaseData;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Order;
import vn.codegym.qlbanhang.entity.NoteType;
import vn.codegym.qlbanhang.model.NoteTypeModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteTypeService extends BaseService {

    public NoteTypeService() {
        super(new NoteTypeModel());

    }

    public void renderNoteTypePage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("/views/note-type/note-type.jsp").forward(req, resp);
            this.searchNoteType(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void renderCreateNoteTypeForm(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("/views/note-type/create-note-type.jsp").forward(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void renderUpdateNoteForm(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("/views/note/update-note.jsp").forward(req, resp);
            Order order = (Order) super.findById(Integer.valueOf(req.getParameter("id")));
            req.setAttribute("updatingNote", order);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void searchNoteType(HttpServletRequest req, HttpServletResponse resp) {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        BaseData baseData = super.doSearch(req, resp, baseSearchDto, NoteType.getSearchColumn());

        if (baseData != null && baseData.getLstData() != null && !baseData.getLstData().isEmpty()) {
            List<NoteType> lstData = new ArrayList<>();
            int index = 1;
            for (BaseEntity baseEntity : baseData.getLstData()) {
                NoteType noteType = (NoteType) baseEntity;
                noteType.setIndex(index++);
                lstData.add(noteType);
            }
            req.setAttribute("lstData", lstData);
        }
    }

    public void createNew(HttpServletRequest req, HttpServletResponse resp) {
        try {
            BaseEntity baseEntity = new Order();
            Map<String, Object> mapValue = new HashMap<>();
            mapValue.put("name", req.getAttribute("name"));
            mapValue.put("description", req.getAttribute("description"));
            baseEntity.setMapValue(mapValue);
            int save = super.save(baseEntity);
            if (save == 1) {
                this.searchNoteType(req, resp);
                resp.sendRedirect(req.getContextPath() + "/note-type");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

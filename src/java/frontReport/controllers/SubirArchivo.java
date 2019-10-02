/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontReport.controllers;

import com.sun.media.sound.InvalidFormatException;
import frontReport.valueobject.ColumnaVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author victor
 */
public class SubirArchivo extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SubirArchivo</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SubirArchivo at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, InvalidFormatException {
        String rutaArchivo = "";
        HttpSession session = request.getSession();

        try {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            String uploadPath = getServletContext().getRealPath("") + File.separator + "archivos";

// req es la HttpServletRequest que recibimos del formulario.
// Los items obtenidos serán cada uno de los campos del formulario,
// tanto campos normales como ficheros subidos.
            List items = upload.parseRequest(request);

// Se recorren todos los items, que son de tipo FileItem
            for (Object item : items) {
                FileItem uploaded = (FileItem) item;

                // Hay que comprobar si es un campo de formulario. Si no lo es, se guarda el fichero
                // subido donde nos interese
                if (!uploaded.isFormField()) {
                    // No es campo de formulario, guardamos el fichero en algún sitio
                    File fichero = new File(uploadPath, uploaded.getName());
                    rutaArchivo = fichero.getAbsolutePath();
                    try {
                        uploaded.write(fichero);
                    } catch (Exception ex) {
                        Logger.getLogger(SubirArchivo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    // es un campo de formulario, podemos obtener clave y valor
                    String key = uploaded.getFieldName();
                    String valor = uploaded.getString();
                }
            }

        } catch (FileUploadException ex) {
            Logger.getLogger(SubirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setAttribute("rutaArchivo", rutaArchivo);
        request.setAttribute("columnasFiltrables", listarColumnasFiltrables(rutaArchivo));
        request.getRequestDispatcher("vistas/sel_fil_rep.jsp").forward(request, response);
    }

    public List listarColumnasFiltrables(String ruta) throws IOException, InvalidFormatException {
        InputStream excelStream = null;
        List nombreColumnas = new ArrayList<ColumnaVO>();
        try {
            excelStream = new FileInputStream(new File(ruta));
            Workbook workbook = WorkbookFactory.create(excelStream);
            Sheet sheet = workbook.getSheetAt(0);
            Cell cell;
            Row row = sheet.getRow(0);
            Column column;
            for (int i = 0; i < row.getLastCellNum(); i++) {
                cell = row.getCell(i);
                if (cell != null) {
                    ColumnaVO columnaVO = new ColumnaVO();
                    columnaVO.setIdColumna(i);
                    columnaVO.setNombreColumna(cell.getRichStringCellValue().getString());
                    nombreColumnas.add(columnaVO);
                }
            }
        } catch (FileNotFoundException ex) {
        } catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException ex) {
            Logger.getLogger(SubirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(SubirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                excelStream.close();
            } catch (IOException ex) {
            }
        }
        return nombreColumnas;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

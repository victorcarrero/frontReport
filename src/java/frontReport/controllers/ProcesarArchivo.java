/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontReport.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import static org.apache.poi.ss.usermodel.TableStyleType.headerRow;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author victor
 */
public class ProcesarArchivo extends HttpServlet {

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
            out.println("<title>Servlet ProcesarArchivo</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProcesarArchivo at " + request.getContextPath() + "</h1>");
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
        InputStream excelStream = null;
        TreeMap estados = new TreeMap< String, String>();
        HttpSession session = request.getSession();
        int columna = Integer.parseInt(request.getParameter("idColumna"));
        System.out.println("===============");
        try {
            excelStream = new FileInputStream(new File((String) session.getAttribute("rutaArchivo")));
            Workbook workbook = WorkbookFactory.create(excelStream);
            Sheet sheet = workbook.getSheetAt(0);
            Cell cell;
            Row row = null;
            Column column;
            for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
                row = sheet.getRow(i);
                if (row != null && i != 65535) {
                    cell = row.getCell(columna);
                    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        estados.put(String.valueOf(cell.getNumericCellValue()), i);
                    } else if (cell.getCellTypeEnum() == CellType.STRING) {
                        estados.put(cell.getRichStringCellValue().getString(), i);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
        } catch (InvalidFormatException ex) {
            Logger.getLogger(ProcesarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(ProcesarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                excelStream.close();
            } catch (IOException ex) {
            }
        }

        String json = new Gson().toJson(estados);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);

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
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        System.out.println("filtrar");
        String ruta = (String) session.getAttribute("rutaArchivo");
        String idColumna = request.getParameter("idColumna");
        String filtro = request.getParameter("filtro");
        List filasFiltradas = extraerFilas(ruta, Integer.parseInt(idColumna), filtro);
        JsonArray jsonArray = extraerInformacion(filasFiltradas, ruta);
        System.out.println(jsonArray.toString());
        escribirReporte(jsonArray, getServletContext().getRealPath("") + File.separator + "archivos");
        System.out.println("filtro");
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

    public List extraerFilas(String ruta, int columna, String filtro) throws IOException {

        InputStream excelStream = null;
        ArrayList<Integer> filasFiltradas = new ArrayList<Integer>();
        try {
            System.out.println(ruta);
            excelStream = new FileInputStream(new File(ruta));
            Workbook workbook = WorkbookFactory.create(excelStream);
            Sheet sheet = workbook.getSheetAt(0);
            Cell cell;
            Row row = null;
            Column column;
            for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
                row = sheet.getRow(i);
                if (row != null && i != 65535) {
                    cell = row.getCell(columna);
                    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (String.valueOf(cell.getNumericCellValue()).equalsIgnoreCase(filtro)) {
                            filasFiltradas.add(i);
                        }
                    } else if (cell.getCellTypeEnum() == CellType.STRING) {
                        if (cell.getRichStringCellValue().getString().equalsIgnoreCase(filtro)) {
                            filasFiltradas.add(i);
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
        } catch (InvalidFormatException ex) {
            Logger.getLogger(ProcesarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(ProcesarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                excelStream.close();
            } catch (IOException ex) {
            }
        }

        return filasFiltradas;

    }

    public JsonArray extraerInformacion(List filasFiltradas, String ruta) throws IOException {
        JsonArray jsonArray = new JsonArray();
        InputStream excelStream = null;
        try {
            excelStream = new FileInputStream(new File((String) ruta));
            Workbook workbook = WorkbookFactory.create(excelStream);
            Sheet sheet = workbook.getSheetAt(0);

            Cell cell;
            Row row = null;
            Column column;
            for (int i = 0; i < filasFiltradas.size(); i++) {
                row = sheet.getRow((int) filasFiltradas.get(i));
                if (row != null) {
                    JsonObject jsonObject = new JsonObject();
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        cell = row.getCell(j);
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            jsonObject.addProperty(String.valueOf(j), String.valueOf(cell.getNumericCellValue()));
                        } else if (cell.getCellTypeEnum() == CellType.STRING) {
                            jsonObject.addProperty(String.valueOf(j), cell.getStringCellValue());
                        }
                    }
                    jsonArray.add(jsonObject);
                }
            }
        } catch (FileNotFoundException ex) {
        } catch (InvalidFormatException ex) {
            Logger.getLogger(ProcesarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(ProcesarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                excelStream.close();
            } catch (IOException ex) {
            }
        }

        return jsonArray;

    }

    public boolean escribirReporte(JsonArray jsonArray, String ruta) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Hoja excel");

        String[] headers = new String[]{
            "Producto",
            "Precio",
            "Enlace"
        };

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        CellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        ArrayList<String> cabeceras = new ArrayList<String>();
        JsonObject json = (JsonObject) jsonArray.get(0);
        for (String key : json.keySet()) {
            cabeceras.add(key);
        }
        // este va a ser el nombre de las columnas 
        int headersExcel = cabeceras.size();
        HSSFRow headerRow = sheet.createRow(0);
        for (int j = 0; j < headersExcel; j++) {
            String header = cabeceras.get(j);
            HSSFCell cell = headerRow.createCell(j);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            HSSFRow dataRow = sheet.createRow(i + 1);
            for (int k = 0; k < headersExcel; k++) {
                dataRow.createCell(k).setCellValue(jsonArray.get(i).getAsJsonObject().get(cabeceras.get(k)).toString());
            }

        }

        FileOutputStream file = null;
        try {
            file = new FileOutputStream(ruta + File.separator + "data.xls");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcesarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            workbook.write(file);
            System.out.println("escribio el archivo");
        } catch (IOException ex) {
            Logger.getLogger(ProcesarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            file.close();
        } catch (IOException ex) {
            Logger.getLogger(ProcesarArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;

    }
}

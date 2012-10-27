package net;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * this is the connection between client and remote server.
 *
 * @author skuarch
 */
public class connection extends HttpServlet {

    private final static Logger logger = Logger.getLogger(connection.class);

    //==========================================================================
    @Override
    public void init() throws ServletException {

        try {
            PropertyConfigurator.configure(getServletContext().getRealPath("/") + "WEB-INF/log4j.properties");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            super.init();
        }

    } // end init

    //==========================================================================
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        DispatchClient dispatchClient = null;
        String access = null;

        try {

            access = request.getHeader("access");

            if (access != null || access.equalsIgnoreCase("1")) {
                dispatchClient = new DispatchClient(request.getInputStream(), response.getOutputStream());
                dispatchClient.transferData();
            } else {
                response.sendRedirect("index.jsp");
            }

        } catch (Exception e) {
            logger.error(e);
        } finally {
            access = null;
        }

    } // end processRequest

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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

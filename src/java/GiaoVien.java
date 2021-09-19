/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author dangh
 */
@WebServlet(urlPatterns = {"/GiaoVien"})
public class GiaoVien extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            String giaoVien = (String) session.getAttribute("Username");
            String queryString = "";
            queryString = request.getQueryString();
            if(queryString == null)
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<link rel=\"stylesheet\" href=\"css/style_1.css\">");
                //out.println("<link href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css\" rel=\"stylesheet\" id=\"bootstrap-css\">");
                out.println("<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\" id=\"bootstrap-css\">");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
                out.println("<title>Giáo Viên: " + giaoVien + " </title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<nav class='navbar navbar-inverse navbar-fixed-top'>");
                out.println("<div class='container-fluid'>");
                out.println("<div class='navbar-header'>");
                out.println("<a class='navbar-brand' href='#'>" + giaoVien +" </a>");
                out.println("</div>");
                out.println("<ul class='nav navbar-nav'>");
                out.println("<li class='active'><a href='#'>Home</a></li>");
                out.println("<li><a href='DangXuat'>Đăng Xuất</a></li>");
                out.println("</ul>");
                out.println("</div>");
                out.println("</nav>");
                out.println("<br></br>");
                out.println("<br></br>");
                out.println("<div class=text-center>");
                out.println("<h3>Giáo Viên: " + giaoVien + "</h3>");
                out.println("<h4>Danh sách các sinh viên đã tham gia trả lời:</h4>");
                out.println("</div>");
                out.println("<br></br>");
                
                Connection conn = Connect();
                //Show List
                Statement st = conn.createStatement();
                String sql = "SELECT DISTINCT Username FROM TraLoi";
                ResultSet rs = st.executeQuery(sql);
                while(rs.next())
                {
                    String userName = rs.getString("Username");
                    out.println("<div class='alert alert-info col-md-4 col-md-offset-4' role='alert'>");
                    out.println("<div class=text-center>");
                    out.println("<strong><a href='GiaoVien?show=" + userName + "&cauhoi=1'>" + userName + "</a></strong>");
                    out.println("</div>");
                    out.println("</div>");
                    out.println("<br></br>");
                    out.println("<br></br>");
                    
                }
                
                out.println("</body>");
                out.println("</html>");
            }
            else
            {
                String user = request.getParameter("show");
                String sttCauHoi = request.getParameter("cauhoi");
                Connection conn = Connect();
                Statement st = conn.createStatement();
                String cauHoi = "SELECT * FROM CauHoi WHERE STTCauHoi = " + sttCauHoi;
                String traLoi = "SELECT CauTraLoi FROM TraLoi WHERE STTCauHoi = " + sttCauHoi + " AND Username = '" + user + "'";
                ResultSet ch = st.executeQuery(cauHoi);
                String cauHoiHienThi = "";
                String cauTraLoiDung = "";
                String cauTraLoiCuaUser = "";
                if(ch.next())
                {
                    cauHoiHienThi = ch.getString("NoiDungCauHoi");
                    cauTraLoiDung = ch.getString("CauTraLoiChinhXac");
                    cauTraLoiCuaUser = "";
                    ResultSet trl = st.executeQuery(traLoi);
                    if(trl.next())
                        cauTraLoiCuaUser = trl.getString("CauTraLoi");
                }
                int back = Integer.parseInt(sttCauHoi) - 1;
                int next = Integer.parseInt(sttCauHoi) + 1;
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<link rel=\"stylesheet\" href=\"css/style_1.css\">");
                //out.println("<link href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css\" rel=\"stylesheet\" id=\"bootstrap-css\">");
                out.println("<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\" id=\"bootstrap-css\">");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
                out.println("<title>Giáo Viên: " + giaoVien + " </title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<nav class='navbar navbar-inverse navbar-fixed-top'>");
                out.println("<div class='container-fluid'>");
                out.println("<div class='navbar-header'>");
                out.println("<a class='navbar-brand' href='#'>" + giaoVien + " </a>");
                out.println("</div>");
                out.println("<ul class='nav navbar-nav'>");
                out.println("<li class='active'><a href='#'>Home</a></li>");
                out.println("<li><a href='DangXuat'>Đăng Xuất</a></li>");
                out.println("</ul>");
                out.println("</div>");
                out.println("</nav>");
                out.println("<div class='form'>");
                out.println("<div class='form-triangle'></div>");
                out.println("<h2 class='form-header'> Giáo Viên: " + giaoVien + "&nbsp&nbsp&nbsp&nbsp&nbspHọc Sinh: " + user + "</h2>");
                out.println("<form class='form-container' action='GiaoVien' method='get'>");
                out.println("<p><textarea rows=5 cols=90 readonly='readonly'>" + cauHoiHienThi + "</textarea></p>");
                out.println("<p><textarea rows=7 cols=90 readonly='readonly'>" + cauTraLoiCuaUser +"</textarea></p>");
                out.println("<p><textarea rows=7 cols=90 readonly='readonly'>" + cauTraLoiDung +"</textarea></p>");
                out.println("<p hidden><textarea name=show rows=0 cols=0 readonly='readonly'>" + user +"</textarea></p>");
                out.println("<p><button name='cauhoi' value='"+back+"' type='submit' onclick='{document.frm.hdnbt.value=this.value;document.frm.submit();}'>Back</button> <button name='cauhoi' value='"+next+"' type='submit' onclick='{document.frm.hdnbt.value=this.value;document.frm.submit();}'>Next</button></p>");
                out.println("</form>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }
    
    public Connection Connect() throws ClassNotFoundException, SQLException
    {
        String driver = "com.mysql.jdbc.Driver";
        Class.forName(driver);
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "PTUDCSDL2";//ten csdl ma ban can ket noi
        Properties info = new Properties();
        info.setProperty("characterEncoding", "utf8");
        info.setProperty("user", "root");
        info.setProperty("password", "");
        Connection conn = DriverManager.getConnection(url + dbName, info);
        return conn;
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GiaoVien.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GiaoVien.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GiaoVien.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GiaoVien.class.getName()).log(Level.SEVERE, null, ex);
        }
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

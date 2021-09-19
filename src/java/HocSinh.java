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
@WebServlet(urlPatterns = {"/HocSinh"})
public class HocSinh extends HttpServlet {

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
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");  //T_T
        try (PrintWriter out = response.getWriter()) {
            //Mảng lưu kết quả để thả vào HTML.
            //kq[0] câu hỏi.
            //kq[1] câu trả lời nếu có.
            String[] kq = new String[2];    
            String cauTrl = "";
            cauTrl = request.getParameter("traloi");
            HttpSession session = request.getSession();
            String Username = (String) session.getAttribute("Username");
            int count = (int) session.getAttribute("Count");  
            //
            if(request.getParameter("back") != null)    //Click Back.
            {
                //InsertUpdate(count, Username, cauTrl);
                String flag = "back";
                kq = XuLy(count, Username, flag, session, cauTrl);
            }
            else if(request.getParameter("next") != null)   //Click next
            {
                //InsertUpdate(count, Username, cauTrl);
                String flag = "next";
                kq = XuLy(count, Username, flag, session, cauTrl);
            }
            else if(request.getPart("back") == null && request.getParameter("next") == null)
            {
                String flag = null;
                kq = XuLy(count, Username, flag, session, cauTrl);
            }
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" href=\"css/style_1.css\">");
            //out.println("<link href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css\" rel=\"stylesheet\" id=\"bootstrap-css\">");
            out.println("<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\" id=\"bootstrap-css\">");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            out.println("<title>Học Sinh " + Username + " </title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<nav class='navbar navbar-inverse navbar-fixed-top'>");
            out.println("<div class='container-fluid'>");
            out.println("<div class='navbar-header'>");
            out.println("<a class='navbar-brand' href='#'>" + Username +" </a>");
            out.println("</div>");
            out.println("<ul class='nav navbar-nav'>");
            out.println("<li class='active'><a href='#'>Home</a></li>");
            out.println("<li><a href='DangXuat'>Đăng Xuất</a></li>");
            out.println("</ul>");
            out.println("</div>");
            out.println("</nav>");
            out.println("<div class='form'>");
            out.println("<div class='form-triangle'></div>");
            out.println("<h2 class='form-header'>" + Username + "</h2>");
            out.println("<form class='form-container' action='HocSinh' method='post'>");
            out.println("<p><textarea name='cauhoi' rows=5 cols=90 readonly='readonly'>" + kq[0] + "</textarea></p>");
            out.println("<p><textarea name='traloi' rows=7 cols=90>" + kq[1] +"</textarea></p>");
            out.println("<p><button name='back' value='1' type='submit' onclick='{document.frm.hdnbt.value=this.value;document.frm.submit();}'>Back</button> <button name='next' value='1' type='submit' onclick='{document.frm.hdnbt.value=this.value;document.frm.submit();}'>Next</button></p>");
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    //Nhận vào vị trí của câu hiện tại, cờ hiệu back hay next.
    //Load câu hỏi và câu trả lời, sửa biến session.
    public String[] XuLy(int count, String Username, String flag, HttpSession session, String cauTrl) throws ClassNotFoundException, SQLException
    {
        int cauTruoc = count;
        if(null == flag)
            count = 1;
        else switch (flag) {
            case "back":
                count = count - 1;
                break;
            case "next":
                count = count + 1;
                break;
            default:
                count = 1;
                break;
        }
        if(count <= 0)  //Trường hợp đang ở câu 1 nhưng bấm back.
            count = 1;
        System.out.println(count);
        String driver = "com.mysql.jdbc.Driver";
        Class.forName(driver);
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "PTUDCSDL2";//ten csdl ma ban can ket noi
        Properties info = new Properties();
        //info.setProperty("characterEncoding", "utf-8");
        info.setProperty("user", "root");
        info.setProperty("password", "");
        Connection conn = DriverManager.getConnection(url + dbName + "?useUnicode=true&characterEncoding=UTF-8", info);
        Statement st = conn.createStatement();
        //Update, add câu trả lời vào database.
        //Kiểm tra xem trong database đã có câu trả lời của câu đó chưa.
        String check = "SELECT * FROM TraLoi Where Username = '" + Username + "' AND STTCauHoi = " + cauTruoc;
        ResultSet rsCheck = st.executeQuery(check);
        System.out.println(rsCheck);
        if(!rsCheck.next()) //Chưa có câu trả lời trong csdl.
        {
            String ins = String.format("INSERT INTO TraLoi (Username, STTCauHoi, CauTraLoi) VALUES ('%s', %d, '%s')", Username, cauTruoc, cauTrl);
            int kq = st.executeUpdate(ins);
        }
        else    //Đã có câu trả lời -> update.
        {
            String ins = String.format("UPDATE TraLoi SET CauTraLoi = '%s' WHERE Username = '%s' AND STTCauHoi = %d ", cauTrl, Username, cauTruoc);
            int kq = st.executeUpdate(ins);
        }
        //Load câu hỏi và câu trả lời.
        //InsertUpdate(cauTruoc, Username, cauTrl, conn);
        String sql = "SELECT * FROM CauHoi WHERE STTCauHoi = " + count;
        ResultSet rs = st.executeQuery(sql);
        String[] kq = new String[2];
        if(rs.next())
        {
            //count = count + 1;
            kq[0] = rs.getString("NoiDungCauHoi");
            String sql2 = "SELECT * FROM TraLoi WHERE STTCauHoi = " + count + " AND Username = '" + Username +"'";
            ResultSet rs2 = st.executeQuery(sql2);
            if(rs2.next())
                kq[1] = rs2.getString("CauTraLoi");
            session.setAttribute("Count", count);   //Update session.
        }
        System.out.println(count);
        return kq;
    }
    
    public void InsertUpdate(int cauTruoc, String Username, String cauTrl) throws SQLException, ClassNotFoundException
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
        Statement st = conn.createStatement();
        String sql = "SELECT CauTraLoi FROM TraLoi Where Username = '" + Username + "' AND STTCauHoi = " + cauTruoc;
        ResultSet rs = st.executeQuery(sql);
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
            Logger.getLogger(HocSinh.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(HocSinh.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HocSinh.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(HocSinh.class.getName()).log(Level.SEVERE, null, ex);
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

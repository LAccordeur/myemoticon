package com.emoticon.photo.action;

import com.emoticon.photo.domain.Image;
import com.emoticon.photo.domain.User;
import com.emoticon.photo.service.ImageService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by L'Accordeur on 2016/12/17.
 */
@WebServlet(value = "/ImageAction")
@MultipartConfig
public class ImageAction extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        //接收请求类型参数
        Integer type = Integer.valueOf(request.getParameter("type"));

        ImageService imageService = new ImageService();
        if (type == 1) {    //上传图片
            String imageName = request.getParameter("image_name");
            Part image = request.getPart("image");

            //封装图片属性信息
            Image img = new Image();
            img.setDate(new Date());
            img.setName(imageName);
            img.setUser((User) request.getSession().getAttribute("user"));
            img.setUserID(img.getUser().getId() + "");
            img.setUrl(img.getUser().getUsername() + "/" + UUID.randomUUID());

            //上传图片
            imageService.addImage(img, image.getInputStream());

            request.getSession().setAttribute("imageList", imageService.getByUserId(img.getUser().getId()));
            response.sendRedirect(request.getContextPath() + "/home.jsp");

        } else if (type == 2) {    //删除图片

            String ids = request.getParameter("ids");
            String urls = request.getParameter("urls");
            imageService.delByIdsAndUrls(ids, urls);
            request.getSession().setAttribute("imageList", imageService.getByUserId(((User) request.getSession().getAttribute("user")).getId()));

        } else if (type == 3) {

            //获取所有图片用于图片广场展示
            request.getSession().setAttribute("allImages", imageService.getAllImages());
            response.sendRedirect(request.getContextPath() + "/square.jsp");

        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

}

package mobile.model.Entity;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;

@RestResource(exported=false)
@Document(collection = "novels")
public class Novel {
    @Id
    protected ObjectId _id;
    protected int danhgia;
    protected String hinhanh;
    protected int luotdoc;
    @DBRef
    protected User nguoidangtruyen;
    protected String noidung;
    protected int soluongdanhgia;
    protected String tacgia;
    protected String tentruyen;
    protected String theloai;
    protected String trangthai;
    protected String status;
    protected String url;
    @CreatedDate
    protected Date createAt;
    @LastModifiedDate
    protected Date updateAt;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Novel() {
    }

    public Novel(ObjectId _id, int danhgia, String hinhanh, int luotdoc, User nguoidangtruyen, String noidung, int soluongdanhgia, String tacgia, String tentruyen, String theloai, String trangthai, String status,String url,Date createAt, Date updateAt) {
        this._id = _id;
        this.danhgia = danhgia;
        this.hinhanh = hinhanh;
        this.luotdoc = luotdoc;
        this.nguoidangtruyen = nguoidangtruyen;
        this.noidung = noidung;
        this.soluongdanhgia = soluongdanhgia;
        this.tacgia = tacgia;
        this.tentruyen = tentruyen;
        this.theloai = theloai;
        this.trangthai = trangthai;
        this.status = status;
        this.url = url;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public ObjectId getId() {
        return _id;
    }
    public void setId(ObjectId id) {
        this._id = id;
    }

    public Date getCreateAt() {        return createAt;    }

    public void setCreateAt(Date createAt) {        this.createAt = createAt;    }

    public Date getUpdateAt() {        return updateAt;    }

    public void setUpdateAt(Date updateAt) {        this.updateAt = updateAt;    }

    public int getDanhgia() {
        return danhgia;
    }

    public void setDanhgia(int danhgia) {
        this.danhgia = danhgia;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public int getLuotdoc() {
        return luotdoc;
    }

    public void setLuotdoc(int luotdoc) {
        this.luotdoc = luotdoc;
    }

    public User getNguoidangtruyen() {
        return nguoidangtruyen;
    }

    public void setNguoidangtruyen(User nguoidangtruyen) {
        this.nguoidangtruyen = nguoidangtruyen;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public int getSoluongdanhgia() {
        return soluongdanhgia;
    }

    public void setSoluongdanhgia(int soluongdanhgia) {
        this.soluongdanhgia = soluongdanhgia;
    }

    public String getTacgia() {
        return tacgia;
    }

    public void setTacgia(String tacgia) {
        this.tacgia = tacgia;
    }

    public String getTentruyen() {
        return tentruyen;
    }

    public void setTentruyen(String tentruyen) {
        this.tentruyen = tentruyen;
    }

    public String getTheloai() {
        return theloai;
    }

    public void setTheloai(String theloai) {
        this.theloai = theloai;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
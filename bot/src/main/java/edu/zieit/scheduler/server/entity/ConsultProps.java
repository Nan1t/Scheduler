package edu.zieit.scheduler.server.entity;

public class ConsultProps {

    private String url;
    private Point dayPoint;
    private Point teacherPoint;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Point getDayPoint() {
        return dayPoint;
    }

    public void setDayPoint(Point dayPoint) {
        this.dayPoint = dayPoint;
    }

    public Point getTeacherPoint() {
        return teacherPoint;
    }

    public void setTeacherPoint(Point teacherPoint) {
        this.teacherPoint = teacherPoint;
    }

    public static class Point {

        private int col;
        private int row;

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }
    }
}

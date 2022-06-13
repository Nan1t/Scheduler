package edu.zieit.scheduler.server.entity;


import java.util.List;

public class CoursesProps {

    private List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public static class Course {

        private String url;
        private String name;
        private Point dayPoint;
        private Point groupPoint;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Point getDayPoint() {
            return dayPoint;
        }

        public void setDayPoint(Point dayPoint) {
            this.dayPoint = dayPoint;
        }

        public Point getGroupPoint() {
            return groupPoint;
        }

        public void setGroupPoint(Point groupPoint) {
            this.groupPoint = groupPoint;
        }
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

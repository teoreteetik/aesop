package ee.lis.mylab_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyLabMessages {

    public static class MyLabResultMsg implements Serializable {
        public final Order order;
        public MyLabResultMsg(Order order) {
            this.order = order;
        }
        public MyLabResultMsg() {
            this(null);
        }

        @Override
        public String toString() {
            return "MyLabResultMsg{" +
                "order=" + order +
                '}';
        }
    }

    public static class MyLabOrderMsg implements Serializable {
        public final Order order;
        public MyLabOrderMsg(Order order) {
            this.order = order;
        }
        public MyLabOrderMsg() {
            this(null);
        }
    }

    public static class MyLabQueryMsg implements Serializable {
        public final List<String> specimenIds;
        public final List<String> analysisCodes;
        public MyLabQueryMsg(List<String> specimenIds, List<String> analysisCodes) {
            this.specimenIds = Collections.unmodifiableList(specimenIds);
            this.analysisCodes = Collections.unmodifiableList(analysisCodes);
        }
        public MyLabQueryMsg() {
            this(new ArrayList<>(), new ArrayList<>());
        }
    }

    public static class Order {
        public final Patient patient;
        public final List<Container> containers;
        public Order(Patient patient, List<Container> containers) {
            this.patient = patient;
            this.containers = containers;
        }
        public Order() {
            this(null, null);
        }

        @Override
        public String toString() {
            return "Order{" +
                "patient=" + patient +
                ", containers=" + containers +
                '}';
        }
    }

    public static class Container {
        public final String specimenId;
        public final List<Analysis> analyses;
        public Container(String specimenId, List<Analysis> analyses) {
            this.specimenId = specimenId;
            this.analyses = analyses;
        }
        public Container() {
            this(null, null);
        }

        @Override
        public String toString() {
            return "Container{" +
                "specimenId='" + specimenId + '\'' +
                ", analyses=" + analyses +
                '}';
        }
    }

    public static class Patient {
        public final String firstName;
        public final String surname;
        public final String patientId;
        public Patient(String firstName, String surname, String patientId) {
            this.firstName = firstName;
            this.surname = surname;
            this.patientId = patientId;
        }
        public Patient() {
            this(null, null, null);
        }

        @Override
        public String toString() {
            return "Patient{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", patientId='" + patientId + '\'' +
                '}';
        }
    }

    public static class Analysis {
        public final String code;
        public final String name;
        public final Result result;
        public Analysis(String code, String name, Result result) {
            this.code = code;
            this.name = name;
            this.result = result;
        }
        public Analysis() {
            this(null, null, null);
        }

        @Override
        public String toString() {
            return "Analysis{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", result=" + result +
                '}';
        }
    }

    public static class Result {
        public final String value;
        public final String unit;
        public Result(String value, String unit) {
            this.value = value;
            this.unit = unit;
        }
        public Result() {
            this(null, null);
        }

        @Override
        public String toString() {
            return "Result{" +
                "value='" + value + '\'' +
                ", unit='" + unit + '\'' +
                '}';
        }
    }
}
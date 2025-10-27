import model.*;
import service.EmployeeService;

public class Main {
    public static void main(String[] args) {
        EmployeeService service = new EmployeeService();

        service.addEmployee(new Employee("Alex", "Rogowski", "alex.rog@neuronlabs.com", "NeuronLabs", Position.PREZES, 25500));
        service.addEmployee(new Employee("Julia", "Tarnowska", "julia.t@quantumsoft.pl", "QuantumSoft", Position.WICEPREZES, 18200));
        service.addEmployee(new Employee("Mateusz", "Urban", "m.urban@aicode.io", "AICode", Position.MANAGER, 12100));
        service.addEmployee(new Employee("Sara", "Chmiel", "sara.chm@pixelwave.com", "PixelWave", Position.PROGRAMISTA, 8600));
        service.addEmployee(new Employee("Dominik", "Szulc", "dominik.sz@neuronlabs.com", "NeuronLabs", Position.STAZYSTA, 3100));

        service.printAllEmployees();

        System.out.println("\nPracownicy z firmy TechCorp:");
        service.findByCompany("TechCorp").forEach(System.out::println);

        System.out.println("\nSortowanie alfabetyczne po nazwisku:");
        service.sortByLastName().forEach(System.out::println);

        System.out.println("\nGrupowanie po stanowisku:");
        service.groupByPosition().forEach((pos, list) ->
                System.out.println(pos + " -> " + list.size() + " pracowników"));

        System.out.println("\nLiczba pracowników na stanowisku:");
        service.countByPosition().forEach((pos, count) ->
                System.out.println(pos + ": " + count));

        System.out.println("\nŚrednie wynagrodzenie: " + service.getAverageSalary() + " zł");

        service.getHighestPaidEmployee().ifPresent(emp ->
                System.out.println("\nNajlepiej opłacany: " + emp));
    }
}

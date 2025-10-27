import model.*;
import service.EmployeeService;

public class Main {
    public static void main(String[] args) {
        EmployeeService service = new EmployeeService();

        service.addEmployee(new Employee("Jan", "Kowalski", "jan.k@techcorp.com", "TechCorp", Position.PREZES, 27000));
        service.addEmployee(new Employee("Anna", "Nowak", "anna.n@techcorp.com", "TechCorp", Position.MANAGER, 13000));
        service.addEmployee(new Employee("Marek", "Wiśniewski", "marek.w@devsoft.com", "DevSoft", Position.PROGRAMISTA, 9000));
        service.addEmployee(new Employee("Ola", "Zielińska", "ola.z@techcorp.com", "TechCorp", Position.STAZYSTA, 3200));

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

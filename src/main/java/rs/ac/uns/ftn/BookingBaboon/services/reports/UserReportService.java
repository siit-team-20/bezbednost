package rs.ac.uns.ftn.BookingBaboon.services.reports;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.Report;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.UserReport;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IGuestReportService;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IHostReportService;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IUserReportService;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class UserReportService implements IUserReportService {
    private final IHostReportService hostReportService;
    private final IGuestReportService guestReportService;

    @Override
    public Collection<Report> getAll() {
        Collection<Report> reports = new ArrayList<>();
        reports.addAll(hostReportService.getAll());
        reports.addAll(guestReportService.getAll());
        return reports;
    }
}

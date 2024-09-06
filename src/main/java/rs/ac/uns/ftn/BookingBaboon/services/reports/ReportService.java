package rs.ac.uns.ftn.BookingBaboon.services.reports;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.Report;
import rs.ac.uns.ftn.BookingBaboon.repositories.reports.IReportRepository;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IReportService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ReportService implements IReportService {

    private final IReportRepository reportRepository;
    @Override
    public Collection<Report> getAll() {
        return new ArrayList<>();
    }

    @Override
    public Report get(Long reportId) {
        return new Report();
    }

    @Override
    public Report create(Report report) {
        return new Report();
    }

    @Override
    public Report update(Report report) {
        return new Report();
    }

    @Override
    public Report remove(Long reportId) {
        return new Report();
    }
}

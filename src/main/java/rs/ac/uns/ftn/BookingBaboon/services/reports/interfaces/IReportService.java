package rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces;


import rs.ac.uns.ftn.BookingBaboon.domain.reports.Report;

import java.util.Collection;
import java.util.Set;

public interface IReportService {
    Collection<Report> getAll();
    Report get(Long reportId);

    Report create(Report report);

    Report update(Report report);

    Report remove(Long reportId);
}

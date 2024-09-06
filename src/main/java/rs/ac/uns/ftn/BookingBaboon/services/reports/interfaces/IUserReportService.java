package rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces;

import rs.ac.uns.ftn.BookingBaboon.domain.reports.Report;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.UserReport;

import java.util.Collection;

public interface IUserReportService {
    Collection<Report> getAll();
}

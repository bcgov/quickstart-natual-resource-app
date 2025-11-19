package ca.bc.gov.nrs.hrs.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * QueryConstants is a utility class that holds SQL query fragments and constants used throughout
 * the application for querying reporting units, user information, and district data related to
 * waste assessment and reporting.
 *
 * <p>This class is not meant to be instantiated, hence the private constructor.
 * The constants defined in this class are used in repository classes to fetch data from
 * the database.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryConstants {

  private static final String PAGINATION = "OFFSET :page ROWS FETCH NEXT :size ROWS ONLY";
  private static final String COUNT = "SELECT COUNT(1) AS total ";
  private static final String COUNT_CTE = "SELECT COUNT(1) OVER() AS total ";

}

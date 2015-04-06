package ee.lis.flow_component.lis2a2_to_string.lis2a2_description;

import ee.lis.util.template_engine.Builder.*;

import static ee.lis.util.template_engine.Builder.*;

public class LIS2A2RecordDescriptions {

    public static final CompositeTemplateBuilder H;
    public static final CompositeTemplateBuilder P;
    public static final CompositeTemplateBuilder O;
    public static final CompositeTemplateBuilder R;
    public static final CompositeTemplateBuilder C;
    public static final CompositeTemplateBuilder Q;
    public static final CompositeTemplateBuilder L;

    private static final CompositeTemplateBuilder ADDRESS_FIELD;
    private static final CompositeTemplateBuilder VALUE_AND_UNIT;
    private static final CompositeTemplateBuilder CODE_AND_TEXT;
    private static final CompositeTemplateBuilder UNIVERSAL_TEST_ID;
    private static final CompositeTemplateBuilder PHYSICIAN_ID;
    private static final DateTemplateBuilder yyyyMMdd;
    private static final DateTemplateBuilder yyyyMMddHHmmss;
    private static final String REPEAT_DELIMITER = "\\";

    static {
        ADDRESS_FIELD = comp("{STREET_ADDRESS}^{CITY}^{STATE}^{ZIP}^{COUNTRY_CODE}").
                            sub("STREET_ADDRESS", simple()).
                            sub("CITY", simple()).
                            sub("STATE", simple()).
                            sub("ZIP", simple()).
                            sub("COUNTRY_CODE", simple());
        VALUE_AND_UNIT = comp("{VALUE}^{UNIT}").
                             sub("VALUE", simple()).
                             sub("UNIT", simple());
        CODE_AND_TEXT = comp("{CODE}^{TEXT}").
                            sub("CODE", simple()).
                            sub("TEXT", simple());
        UNIVERSAL_TEST_ID = comp("{LOINC}^{NAME}^{TYPE}^{LOCAL_CODE}").
                                sub("LOINC", simple()).      // This field is currently unused but reserved for the application of a universal test identifier code (LOINC Codes), should one system become available for use at a future time.
                                sub("NAME", simple()).       // This would be the test or battery name associated with the universal test ID code described in LOINC.
                                sub("TYPE", simple()).       // In the case where multiple national or international coding schemes exist, this field may be used to determine what coding scheme is employed in the test ID and test ID name fields.
                                sub("LOCAL_CODE", simple()); // This is the code defined by the manufacturer. This code may be a number, characters, or a multiple test designator based on manufacturer-defined delimiters.
        PHYSICIAN_ID = comp("{ID_NUMBER}^{SURNAME}^{FIRST_NAME}^{MIDDLE_NAME}^{SUFFIX}^{TITLE}").
                           sub("ID_NUMBER", simple()).
                           sub("SURNAME", simple()).
                           sub("FIRST_NAME", simple()).
                           sub("MIDDLE_NAME", simple()).
                           sub("SUFFIX", simple()).
                           sub("TITLE", simple());
        yyyyMMdd = date("yyyyMMdd");
        yyyyMMddHHmmss = date("yyyyMMddHHmmss");

        H = comp("H|\\^&|{H.3}|{H.4}|{H.5}|{H.6}|{H.7}|{H.8}|{H.9}|{H.10}|{H.11}|{H.12}|{H.13}|{H.14}\r").
                sub("H.3", simple()).                               // Message Control ID. This is a unique number or other ID that uniquely identifies the transmission for use in network systems that have defined acknowledgment protocols.
                sub("H.4", simple()).                               // Access Password. This is a level security/access password as mutually agreed upon by the sender and receiver. If this security check fails, the transmission will be aborted, and the sender will be notified of an access violation.
                sub("H.5", simple()).                               // Sender Name or ID. The purpose of this field is to define the manufacturer/instrument(s) specific to this line. Using repeat and/or component delimiters, this field may reflect software or firmware revisions, multiple instruments available on the line, etc.
                sub("H.6", ADDRESS_FIELD).                          // This text value shall contain the street address of the sender.
                sub("H.7", simple()).                               // Reserved Field. This field is currently unused but reserved for future use.
                sub("H.8", repeat(REPEAT_DELIMITER, simple())).     // Patient Telephone Number
                sub("H.9", simple()).                               // Characteristics of Sender. This field contains any characteristics of the sender such as, parity, checksums, optional protocols, etc. necessary for establishing a communication link with the sender.
                sub("H.10", simple()).                              // Receiver ID. This text value includes the name or other ID of the receiver. Its purpose is verification that the transmission is indeed for the receiver.
                sub("H.11", simple()).                              // Comment or Special Instructions. This text field shall contain any comments or special instructions relating to the subsequent records to be transmitted.
                sub("H.12", simple()).                  // Processing ID. The processing ID indicates how this message is to be processed: P - Production: Treat message as active message to be completed according to standard processing, T - Training: Message is initiated by a trainer and should not have an effect on the system, D - Debugging: Message is initiated for the purpose of a debugging program, Q - Quality Control: Message is initiated for the purpose of transmitting quality control/quality assurance or regulatory data.
                sub("H.13", simple()).            // Version Number. This value identifies the version level of the specification. This value is currently LIS2-A2
                sub("H.14", yyyyMMddHHmmss); // Date and Time of Message. This field contains the date and time that the message was generated

        P = comp("P|{P.2}|{P.3}|{P.4}|{P.5}|{P.6}|{P.7}|{P.8}|{P.9}|{P.10}|{P.11}|{P.12}|{P.13}|{P.14}|{P.15}|{P.16}|{P.17}|{P.18}|{P.19}|{P.20}|{P.21}|{P.22}|{P.23}|{P.24}|{P.25}|{P.26}|{P.27}|{P.28}|{P.29}|{P.30}|{P.31}|{P.32}|{P.33}|{P.34}|{P.35}\r").
                sub("P.2", simple()).             // Sequence number
                sub("P.3", simple()).             // Practice-Assigned Patient ID. This identifier shall be the unique ID assigned and used by the practice to identify the patient and his/her results upon return of the results of testing.
                sub("P.4", simple()).             // Laboratory-Assigned Patient ID. This identifier shall be the unique processing number assigned to the patient by the laboratory.
                sub("P.5", simple()).             // Patient ID Number 3. This field shall be optionally used for additional, universal, or manufacturer-defined identifiers (such as the social security account no.), as arranged between the transmitter and the receiver. Please note that individuals are not required to provide social security numbers.
                sub("P.6", comp("{SURNAME}^{FIRST_NAME}^{MIDDLE_NAME}^{SUFFIX}^{TITLE}"). //Patient Name.
                               sub("SURNAME", simple()).
                               sub("FIRST_NAME", simple()).
                               sub("MIDDLE_NAME", simple()).
                               sub("SUFFIX", simple()).
                               sub("TITLE", simple())).
                sub("P.7", simple()).                            //Mother's Maiden Name. The optional mother’s maiden name may be required to distinguish between patients with the same birthdate and last name when registry files are very large. This name shall be presented as the mother’s maiden surname, for example, Thompson.
                sub("P.8", yyyyMMdd).                            //Birthdate
                sub("P.9", simple()).                            //Patient Sex. This field shall be represented by M, F, or U.
                sub("P.10", repeat("^", simple())).              //Patient Race-Ethnic Origin. The following examples may be used: W - white, B - black, O - Asian/Pacific Islander, NA - Native American/Alaskan Native, H - Hispanic. Full text names of other ethnic groups may also be entered. Note that multiple answers are permissible.
                sub("P.11", ADDRESS_FIELD).                      //This text value shall record the street address of the patient's mailing address
                sub("P.12", simple()).                           //Reserved Field
                sub("P.13", repeat(REPEAT_DELIMITER, simple())). //Patient Telephone Number(s)
                sub("P.14", repeat(REPEAT_DELIMITER, simple())). //Attending Physician ID. This field shall identify the physician(s) caring for the patient as either names or codes, as agreed upon between the sender and the receiver. Multiple physician names (for example, ordering physician attending physician, referring physician) shall be separated by repeat delimiters.
                sub("P.15", simple()).                           //Special Field 1. This is an optional text field for vendor use (each laboratory can use this differently).
                sub("P.16", simple()).                           //Special Field 2.This is an optional text field for vendor use.
                sub("P.17", VALUE_AND_UNIT).                     //Patient Height. This is an optional numeric field containing the patient’s height. The default units are centimeters. If measured in terms of another unit, the units should also be transmitted
                sub("P.18", VALUE_AND_UNIT).                     //Patient Weight. This is an optional numeric field containing the patient’s weight. The default units are kilograms. If measured in terms of another unit, for example, pounds, the unit name shall also be transmitted
                sub("P.19", repeat(REPEAT_DELIMITER, simple())). //Patient’s Known or Suspected Diagnosis
                sub("P.20", simple()).                           //Patient Active Medications. This field is used for patient active medications or those suspected, in overdose situations. The generic name shall be used. This field is of use in interpretation of clinical results.
                sub("P.21", simple()).                           //Patient's Diet. This optional field in free text should be used to indicate such conditions that affect results of testing such as 16-hour fast (for triglycerides) and no red meat (for hemoccult testing).
                sub("P.22", simple()).                           //Practice Field Number 1. This is a text field for use by the practice; the optional transmitted text will be returned with the results.
                sub("P.23", simple()).                           //Practice Field Number 2. Same as P.22.
                sub("P.24", comp("{ADMISSION_DATE}^{DISCHARGE_DATE}"). //Admission and Discharge Dates. The discharge date, when included, follows the admission date and is separated from it by a repeat delimiter.
                                sub("ADMISSION_DATE", yyyyMMdd).
                                sub("DISCHARGE_DATE", yyyyMMdd)).
                sub("P.25", simple()).                           //Admission Status. This value shall be represented by the following minimal list or by extensions agreed upon between the sender and receiver: OP (outpatient), PA (preadmit), IP (inpatient), ER (emergency room).
                sub("P.26", simple()).                           //Location. This text value shall reflect the general clinic location or nursing unit, or ward or bed (or both) of the patient in terms agreed upon by the sender and the receiver.")).
                sub("P.27", simple()).                           //Nature of Alternative Diagnostic Code and Classifiers. This field relates to Section P.28. It identifies the class of code or classifiers that are transmitted (e.g. DRGs, or in the future, AVGs [ambulatory visitation groups]).
                sub("P.28", repeat(REPEAT_DELIMITER,
                                comp("{CODE}^{TEST_DESCRIPTORS}"). //Alternative Diagnostic Code and Classification. Alternative diagnostic codes and classifications (e.g., DRG codes) can be included in this field. The nature of the diagnostic code is identified in P.27. If multiple codes are included, they should be separated by repeat delimiters. Individual codes can be followed by optional test descriptors (when the latter are present) and must be separated by component delimiters.
                                    sub("CODE", simple()).
                                    sub("TEST_DESCRIPTORS", repeat("^", simple())))).
                sub("P.29", simple()).                           //Patient religion. When needed, this value shall include the patient’s religion. Codes or names may be sent as agreed upon between the sender and the receiver. Full names of religions may also be sent as required. A list of sample religious codes follows: P - Protestant, C - Catholic, M - Church of the Latter Day Saints (Mormon), J - Jewish, L - Lutheran, H - Hindu.
                sub("P.30", simple()).                           //When required, this value shall indicate the marital status of the patient as follows: M - married, S - single, D - divorced, W - widowed, A - separated.
                sub("P.31", repeat(REPEAT_DELIMITER, simple())). //Isolation codes indicate precautions that must be applied to protect the patient or staff against infection. Multiple precautions can be listed when separated by repeat delimiters. Full text precautions may also be sent.
                sub("P.32", simple()).                           //Language. The value of this field indicates the patient’s primary language. This may be needed when the patient is not fluent in the local language.
                sub("P.33", CODE_AND_TEXT).                      //Hospital Service. This value indicates the hospital service currently assigned to the patient. Both code and text may be sent.
                sub("P.34", CODE_AND_TEXT).                      //Hospital Institution. This value indicates the hospital institution currently assigned to the patient. Both code and text may be sent.
                sub("P.35", simple());                           //Dosage Category. This value indicates the patient dosage group.

        O = comp("O|{O.2}|{O.3}|{O.4}|{O.5}|{O.6}|{O.7}|{O.8}|{O.9}|{O.10}|{O.11}|{O.12}|{O.13}|{O.14}|{O.15}|{O.16}|{O.17}|{O.18}|{O.19}|{O.20}|{O.21}|{O.22}|{O.23}|{O.24}|{O.25}|{O.26}|{O.27}|{O.28}|{O.29}|{O.30}|{O.31}\r").
                sub("O.2", simple()).              // Sequence Number.
                sub("O.3", comp("{SPECIMEN_ID}^{ISOLATE_NUMBER}^{WELL_NUMBER}"). // Specimen ID. This text field shall represent a unique identifier for the specimen assigned by the information system and returned by the instrument. If the specimen has multiple components further identifying cultures derived from it, these component identifiers will follow the specimen ID and be separated by component delimiters. For example, the specimen ID may contain the specimen number followed by the isolate number, well or cup number (for example, 10435Aˆ01ˆ64).
                               sub("SPECIMEN_ID", simple()).
                               sub("ISOLATE_NUMBER", simple()).
                               sub("WELL_NUMBER", simple())).
                sub("O.4", simple()).                                  // Instrument Specimen ID. This text field shall represent a unique identifier assigned by the instrument, if different from the information system identifier, and returned with results for use in referring to any results.
                sub("O.5", repeat(REPEAT_DELIMITER, UNIVERSAL_TEST_ID)).
                sub("O.6", repeat(REPEAT_DELIMITER, simple())).        // Priority. Test priority codes are as follows: S - stat, A - as soon as possible, R - routine, C - callback, P - preoperative. If more than one priority code applies, they must be separated by repeat delimiters.
                sub("O.7", yyyyMMddHHmmss).                              // Requested/Ordered Date and Time. Denotes the date and time the test order should be considered ordered. Usually this will be the date and time the order was recorded. This is the date and time against which the priorities should be considered. If the ordering service wants the test performed at a specified time in the future, for example, a test to be drawn two days in the future at 8 p.m., the future date and time should be recorded here. Note that the message header data and the future date and time should be recorded here. Further, note that the message header record date and time indicates the time the order was transmitted to or from the instrument.
                sub("O.8", yyyyMMddHHmmss).                              // Specimen Collection Date and Time. This field shall represent the actual time the specimen was collected or obtained.
                sub("O.9", yyyyMMddHHmmss).                              // Collection End Time. This field shall contain the end date and time of a timed specimen collection, such as 24-hour urine collection.
                sub("O.10", VALUE_AND_UNIT).                           // Collection Volume. This value shall represent the total volume of specimens such as urine or other bulk collections when only aliquot is sent to the instrument. The default unit of measure is milliliters. When units are explicitly represented, they should be separated from the numeric value by a component delimiter.
                sub("O.11", simple()).                                 // Collector ID.This field shall identify the person and facility which collected the specimen. If there are questions relating to circumstances surrounding the specimen collection, this person will be contacted.
                sub("O.12", simple()).                                 // Action Code. This field shall indicate the action to be taken with respect to the specimens that accompany or precede this request. The following codes shall be used: C - cancel request for the battery or tests named, A - add the requested tests or batteries to the existing specimen with the patient and specimen identifiers and date/time given in this record, N - new requests accompanying a new specimen, P - pending specimen, L - reserved, X - specimen or test already in process, Q treat specimen as a Q/C test specimen
                sub("O.13", simple()).                                 // Danger Code. This field representing either a test or a code shall indicate any special hazard associated with the specimen, for example, a hepatitis patient, suspected anthrax.
                sub("O.14", simple()).                                 // Relevant Clinical Information. Additional information about the specimen would be provided here and used to report information such as amount of inspired O2 for blood gases, point in menstrual cycle for cervical pap tests, or other conditions that influence test interpretations.
                sub("O.15", yyyyMMddHHmmss).                             // Date/Time Specimen Received. This optional field shall contain the actual log-in time recorded in the laboratory.
                sub("O.16", comp("{SPECIMEN_TYPE}^{SPECIMEN_SOURCE}"). // Specimen Descriptor.
                                sub("SPECIMEN_TYPE", simple()).            // Specimen Type. Samples of specimen culture types or sources would be blood, urine, serum, hair, wound, biopsy, sputum etc.
                                sub("SPECIMEN_SOURCE", simple())).         // Specimen Source. This is always the second component of the specimen descriptor field and is used specifically to determine the specimen source body site (e.g., left arm, left hand, right lung).
                sub("O.17", PHYSICIAN_ID).                             //Ordering Physician.
                sub("O.18", repeat(REPEAT_DELIMITER, simple())).       // Physician's Telephone Number(s). This field shall contain the telephone number(s) of the requesting physician and will be used in responding to callback orders and for critically abnormal results
                sub("O.19", simple()).                                 // User Field Number 1. Text sent by the requestor should be returned by the sender along with the response.
                sub("O.20", simple()).                                 // User Field Number 2. Same as O.19.
                sub("O.21", simple()).                                 // Laboratory Field Number 1. This is an optional field definable for any use by the laboratory.
                sub("O.22", simple()).                                 // Laboratory Field Number 2. Same as O.21.
                sub("O.23", yyyyMMddHHmmss).                             // Date/Time Results Reported or Last Modified. This field is used to indicate the date and time the results for the order are composed into a report, or into this message or when a status as defined in O.26 or R.9 is entered or changed. When the information system queries the instrument for untransmitted results, the information in this field may be used to control processing on the communications link. Usually, the ordering service would only want those results for which the reporting date and time is greater than the date and time the inquiring system last received results.
                sub("O.24", simple()).                                 // Instrument Charge to Information System. This field contains the billing charge or accounting reference by this instrument for tests performed.
                sub("O.25", simple()).                                 // Instrument Section ID. This identifier may denote the section of the instrument where the test was performed. In the case where multiple instruments are on a single line or a test was moved from one instrument to another, this field will show which instrument or section of an instrument performed the test.
                sub("O.26", simple()).                                 // Report Types. The following codes shall be used: O - order record; user asking that analysis be performed, C - correction of previously transmitted results, P - preliminary results, F - final results, X - order cannot be done, order cancelled, I - in instrument pending, Y - no order on record for this test (in response to query), Z - no record of this patient (in response to query), Q - response to query (this record is a response to a request-information query).
                sub("O.27", simple()).                                 // Reserved Field. This field is unused but reserved for future expansion.
                sub("O.28", simple()).                                 // Location of Specimen Collection. This field defines the location of specimen collection if different from the patient location.
                sub("O.29", simple()).                                 // Nosocomial Infection Flag. This field is used for epidemiological reporting purposes and will show whether the organism identified is the result of a nosocomial (hospital-acquired) infection.
                sub("O.30", simple()).                                 // Specimen Service. In cases where an individual service may apply to the specimen collected, and the service is different from the patient record service, this field may be used to define the specific service responsible for such collection.
                sub("O.31", simple());                                 // Specimen Institution. In cases where the specimen may have been collected in an institution, and the institution is different from the patient record institution, this field may be used to record the institution of specimen collection.

        R = comp("R|{R.2}|{R.3}|{R.4}|{R.5}|{R.6}|{R.7}|{R.8}|{R.9}|{R.10}|{R.11}|{R.12}|{R.13}|{R.14}\r").
                sub("R.2", simple()).             // Sequence number
                sub("R.3", UNIVERSAL_TEST_ID).    // Universal Test ID
                sub("R.4", simple()).             // Data or Measurement Value
                sub("R.5", simple()).             // Units. The abbreviation of units for numeric results shall appear here. ISO standard abbreviations in accordance with ISO 2955 4 should be employed when available (e.g., use mg rather than milligrams). Units can be reported in upper or lower case.
                sub("R.6", repeat(REPEAT_DELIMITER, comp("{RANGE}^{DESCRIPTION}"). // Reference Ranges. This value shall be reported in the following sample format: (lower limit to upper limit; i.e., 3.5 to 4.5). A result may have multiple ranges. When multiple ranges are sent, they shall be separated by repeat delimiters. Each range can also have a text description. The text description follows immediately after the range, and is separated from it by a component delimiter. Most results will only have one normal range transmitted.
                                                        sub("RANGE", simple()).
                                                        sub("DESCRIPTION", simple()))).
                sub("R.7", simple()).                       // Result Abnormal Flags. This field shall indicate the normalcy status of the result. The characters for representing significant changes either up or down or abnormal values shall be: L - below low normal, H - above high normal, LL - below panic normal, HH - above panic high, < - below absolute low, that is off low scale on instrument, > - above absolute high, that is off high scale on an instrument, N - normal, A - abnormal, U - significant change up, D - significant change down, B - better, use when direction not relevant or not defined, W - worse, use when direction not relevant or not defined. When the instrument can discern the normal status of a textual report, such as microbiological culture, these should be reported as N when normal and A when abnormal.
                sub("R.8", simple()).                       // Nature of Abnormality Testing. The kind of normal testing performed shall use the following representation: A denotes that an age-based population was tested; S, a sex-based population; and R, a race-based population. As many of the codes as apply shall be included. For example, if sex, age, and race normals were tested, an ASR would be transmitted. N implies that generic normal range was applied to all patient specimens.
                sub("R.9", simple()).                       // Result Status. The following codes shall be used: C - correction of previously transmitted results, P - preliminary results, F - final results, X - order cannot be done, I - in instrument, results pending, S - partial results, M - this result is an MIC level, R - this result was previously transmitted, N - this result record contains necessary information to run a new order, NOTE: For example, when ordering a sensitivity, the information system may download a result record containing the organism type, or species, identified in a previous test. Q - this result is a response to an outstanding query, V - operator verified/approved result, W - Warning: Validity is questionable
                sub("R.10", yyyyMMdd).                      // Date of Change in Instrument Normative Values or Units. This field shall remain empty if there are no relevant normals or units. A change in these data from those recorded in the receiving system's dictionary indicates a need for manual review of the results to detect whether they can be considered the same as preceding ones.
                sub("R.11", comp("{PERFORMER}^{VERIFIER}"). // Operator Identification. The first component identifies the instrument operator who performed the test. The second component identifies the verifier for the test.
                                sub("PERFORMER", simple()).
                                sub("VERIFIER", simple())).
                sub("R.12", yyyyMMddHHmmss).                  // Date/Time Test Started. This field records the date and time the instrument started the test for which the results are now being reported.
                sub("R.13", yyyyMMddHHmmss).                  // Date/Time Test Completed. This field records the date and time the instrument completed the test for which the results are now being reported.
                sub("R.14", simple());                      // Instrument Identification. This field identifies the instrument or section of instrument that performed this particular measurement.

        C = comp("C|{C.2}|{C.3}|{C.4}|{C.5}").
                sub("C.2", simple()).             // Sequence Number.
                sub("C.3", simple()).             // Comment Source. This field specifies the comment origination point as follows: P - practice, L - information system, I - clinical instrument system.
                sub("C.4", simple()).             // Comment Text. Where comment codes/mnemonics are used, the code should be sent first, followed, if desired, by the comment text and separated by a component delimiter.
                sub("C.5", simple());             // The following codes may be used to qualify comment record types: G - generic/free result comment, T - result name comment, P - positive result comment, N - negative result comment, I - instrument flag(s) comment

        Q = comp("Q|{Q.2}|{Q.3}|{Q.4}|{Q.5}|{Q.6}|{Q.7}|{Q.8}|{Q.9}|{Q.10}|{Q.11}|{Q.12}|{Q.13}\r").
                sub("Q.2", simple()).             // Sequence number.
                sub("Q.3", repeat(REPEAT_DELIMITER, comp("{PATIENT_ID}^{SPECIMEN_ID}"). // Starting Range ID Number. This field may contain three or more components to define a range of patients/specimens/manufacturers selection criteria. The first component is the information system patient ID number. The second component is the information system specimen ID number. Any further components are manufacturer-defined and for use in request subresult information (that is, an individual isolate/battery for a specimen number). These components are position dependent. A list of sample IDs could be requested by the use of the repeat delimiter to separate IDs.
                                                        sub("PATIENT_ID", simple()).
                                                        sub("SPECIMEN_ID", simple()))).
                sub("Q.4", repeat(REPEAT_DELIMITER, comp("{PATIENT_ID}^{SPECIMEN_ID}"). // Ending Range ID Number. This field is similar to that described in Q.3. If a single result or specimen demographic or test order is being requested, then this field may be left blank.
                                                        sub("PATIENT_ID", simple()).
                                                        sub("SPECIMEN_ID", simple()))).
                sub("Q.5", repeat(REPEAT_DELIMITER, UNIVERSAL_TEST_ID)). // Universal Test ID. This field may alternatively contain multiple codes separated by repeat delimiters, or the field may contain the text ALL, which signifies a request for all results on all tests or batteries for the patients/specimens/tests defined in Sections Q.3 and Q.4 and within the dates described in Sections Q.6 and Q.7.
                sub("Q.6", simple()).                                    // Nature of Request Time Limits. Specify whether the date and time limits specified in Sections Q.7 and Q.8 refer to the specimen collect or ordered date (see Section O.8) or test date (see Section O.23): S indicates the specimen collect date; R indicates the result test date. If nothing is entered, the date criteria are assumed to be the result test date.
                sub("Q.7", repeat(REPEAT_DELIMITER, yyyyMMddHHmmss)).      // Beginning Request Results Date and Time. This field shall represent either a beginning (oldest) date and time for which results are being requested or a single date and time. The field may contain a single date and time or multiple dates and times separated by repeat delimiters. If no date and time is included, the instrument should assume that the information system wants results going as far into the past as possible and consistent with the criteria specified in other fields.
                sub("Q.8", yyyyMMddHHmmss).                                // Ending Request Results Date and Time. This field, if not null, specifies the ending or latest (or most recent) date and time for which results are being requested.
                sub("Q.9", PHYSICIAN_ID).                                // Requesting Physician Name. This field identifies the individual physician requesting the results.
                sub("Q.10", repeat(REPEAT_DELIMITER, simple())).         // Requesting Physician Telephone Number(s)
                sub("Q.11", simple()).                                   // User Field Number 1. This is a user-defined field.
                sub("Q.12", simple()).                                   // User Field Number 2. This is a user-defined field.
                sub("Q.13", simple());                                   // Request Information Status Codes. The following codes shall be used: C - correction of previously transmitted results, P - preliminary results, F - final results, X - results cannot be done, request cancelled, I - request results pending, S - request partial/unfinalized results, M - result is an MIC level, R - this result was previously transmitted, A - abort/cancel last request criteria (allows a new request to follow), N - requesting new or edited results only O - requesting test orders and demographics only (no results) D - requesting demographics only (e.g., patient record)

        L = comp("L|{L.2}|{L.3}\r").
                sub("L.2", simple()).             // Sequence number
                sub("L.3", simple()); // Termination Code. This field provides an explanation of the end of the session. Nil, N - normal termination, T - sender aborted, R - receiver requested abort, E - unknown system error, Q - error in last request for information, I - no information available from last query, F - last request for information processed, NOTE: I or Q will terminate a request and allow processing of a new request record.
    }
}
package com.terragene.baxen.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terragene.baxen.component.LastCheckTimestamp;
import com.terragene.baxen.configuration.HttpGetWithBody;
import com.terragene.baxen.dto.*;
import com.terragene.baxen.entity.*;
import com.terragene.baxen.exception.BaxenGenericException;
import com.terragene.baxen.exception.GenericGetRequestException;
import com.terragene.baxen.exception.ObtenerTokenException;
import com.terragene.baxen.repository.*;
import com.terragene.baxen.util.DateFormatter;
import com.terragene.baxen.util.ProcesosEnum;
import com.terragene.baxen.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TerraService {

    @Value("${spring.external.service.base-url}")
    private String basePath;

    /*Definicion de constantes*/
    private static final String POSITION_NUMBER = "positionNumber";
    private static final String INCUBATOR_SERIAL_NUMBER = "incubatorSerialNumber";
    private static final String DATA_DEVICE = "data_device";
    private static final String DATA = "data";

    private static final String RESULTADO_DATOS_DISP = "resultadoDatosDispositivo";
    private static final String RESULTADO_DATOS_LECTURA = "resultadoDatosLectura";

    private static final String STERILIZER_SERIAL = "sterilizerSerial";
    private static final String STERILIZER_NAME = "sterilizerName";
    private static final String D_VALUE = "dValue";
    private static final String USER_NAME = "userName";

    private static final String TICKET_NUMBER = "ticketNumber";
    private static final String RESULT = "result";
    private static final String PROCESS = "process";
    private static final String CONDITION_SCIB = "conditionSCIB";
    private static final String LOAD_NUMBER = "loadNumber";
    private static final String PROGRAM_NUMBER = "programNumber";
    private static final String STARTED_TIME = "startedTime";
    private static final String AVERAGE_TEMPERATURE = "averageTemperature";
    private static final String RESULT_DATE = "resultDate";
    private static final String PRODUCT_NAME = "productName";
    private static final String PRODUCT_LOT = "productLot";
    private static final String PRODUCT_BRAND = "productBrand";
    private static final String INCUBATOR_NAME = "incubatorName";
    private static final String CYCLE = "cycle";

    private static final String DISINFECTOR_SERIAL = "disinfectorSerial";
    private static final String DISINFECTOR_NAME = "disinfectorName";
    private static final String PEROXIDE_CONCENTRATION = "peroxideConcentration";
    private static final String ROOM_ID = "roomId";
    private static final String ROOM_VOLUME = "roomVolume";
    private static final String WASHER = "washer";
    private static final String WASHER_SERIAL = "washerSerial";
    private static final String NAME_PROTEIN = "nameProtein";
    private static final String SURFACE = "surface";
    private static final String LOTE_PRODUCT = "loteProduct";

    private static final String MANUFACTURE_INDICATOR = "manufactureIndicator";
    private static final String SERIAL_WASHER = "serialWasher";
    public static final String BRAND_WASHER = "brandWasher";
    public static final String MANUFACTURE_DATE = "manufactureDate";
    public static final String WASHING_TIME = "washingTime";
    public static final String DETERGENT_TYPE = "detergentType";
    public static final String DETERGENT_CONCENTRATION = "detergentConcentration";
    public static final String PROGRAM = "program";
    public static final String LOCATION = "location";
    public static final String CREATION_TEST = "creationTest";
    public static final String TEMPERATURE = "temperature";
    public static final String EXPIRATION_DATE = "expirationDate";
    public static final String SERIAL_NUMBER_SCANNER = "serialNumberScanner";
    public static final String LOT_PRODUCT = "lotProduct";
    public static final String BRAND = "brand";
    public static final String WATER_PRESSURE = "waterPressure";
    public static final String WATER_HARDNESS = "waterHardness";
    public static final String SERIAL_STERILIZER = "serialSterilizer";
    public static final String BRAND_STERILIZER = "brandSterilizer";
    public static final String EXPOSURE_TIME = "exposureTime";
    public static final String CONCENTRATION = "concentration";
    public static final String RELATIVE_DAMPNESS = "relativeDampness";
    public static final String PACKAGE_NUMBER = "packageNumber";

    public static final String NAME_PRODUCT = "nameProduct";

    public static final String NO_DATA = "SIN_DATOS";


    /*Logger*/
    private static final Logger logger = LoggerFactory.getLogger(TerraService.class);

    /*Repository*/
    private final RestTemplate restTemplate;
    private final DisinfectionRepository disinfectionRepository;
    private final SterilizationRepository sterilizationRepository;
    private final ProteinRepository proteinRepository;
    private final UsersRepository usersRepository;
    private final SterilizerRepository sterilizerRepository;
    private final AuditRepository auditRepository;
    private final WashingRepository washingRepository;
    private final LastCheckTimestamp lastCheckTimestamp;


    /**
     * Se procesa el mensaje recibido con los dos parametros position & incubatorSerialNumber, llamamos al metodo correspondiente y verificamos que este en la base
     *
     * @param username   nombre de usuario
     * @param statusRead estado de lectura
     * @param password   contraseña
     * @throws ParseException          excepcion de parseo
     * @throws JsonProcessingException excepcion de json
     * @throws JSONException           excepcion de json
     */
    public Map<String, Long> processMessage(String username, String statusRead, String password) throws ParseException, JsonProcessingException, JSONException {
        logger.info("Inicia proceso de datos");

        UsersEntity user = usersRepository.getAll().orElseThrow(() -> new GenericGetRequestException("User not found"));
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //UsersEntity user = usersRepository.findByUsername(username).orElseThrow(() -> new GenericGetRequestException("User not found"));

        /*if (passwordEncoder.matches(password, user.getPassword())) {*/

            LocalDateTime from = LocalDateTime.now();
            LocalDateTime to = LocalDateTime.now().plusDays(Integer.parseInt(user.getDiasFiltro()));

            LocalDateTime startFilter = from.withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endFilter = to.withHour(23).withMinute(59).withSecond(59);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            procesarDatos(username, formatter.format(startFilter), formatter.format(endFilter), statusRead, password, user);

            logger.info("Se guarda los datos en la tabla de auditoria si hay cambios en las novedades y se informara");
            logger.info("Finalizo el proceso de datos");

            return obtenerCambiosRecientes();
    }

    public Map<String, Long> obtenerCambiosRecientes() {
        // Directamente usa LocalDateTime
        LocalDateTime lastChecked = lastCheckTimestamp.getLastChecked();

        // Llama al método actualizado del repositorio
        List<AuditEntity> nuevosLogs = auditRepository.findByCreatedAtAfter(lastChecked);

        // Si hay nuevos registros, agrúpalos por nombre de entidad y muestra la cuenta
        if (!nuevosLogs.isEmpty()) {
            Map<String, Long> conteoPorEntidad = nuevosLogs.stream()
                    .collect(Collectors.groupingBy(AuditEntity::getEntityName, Collectors.counting()));

            logger.info(conteoPorEntidad.toString());
            // Actualizar el timestamp de la última contabilización con el momento actual
            lastCheckTimestamp.updateLastChecked(LocalDateTime.now());

            return conteoPorEntidad;
        }
        return new HashMap<>();
    }


    private void procesarDatos(String username, String from, String to, String statusRead, String password, UsersEntity user) throws ParseException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> resultList = new ArrayList<>();
        TokenDTO token = tokenLogin(new LoginDTO(username, password, user.getDomainDist()));

        /*Api Bi Sterilization*/
        String biSterilization = getBiSterilizationResult(token.getToken(), new GenericDTO(from, to, statusRead));

        List<ResponseBiologicalIndicatorSterilizationDTO> responseBiologicalIndicatorSterilizationDTOSList = filtrarMensaje(mapper, resultList, biSterilization, ProcesosEnum.BI_STERILIZATION_RESULTS);
        processResultsBiologicalIndicatorSterilization(responseBiologicalIndicatorSterilizationDTOSList);

        /*Api Bi Disinfection*/
        String biDisinfection = getBiDisinfectionResults(token.getToken(), new GenericDTO(from, to, statusRead));
        List<ResponseBiologicalIndicatorsOfDisinfectionDTO> responseBiologicalIndicatorsOfDisinfectionDTOList = filtrarMensaje(mapper, resultList, biDisinfection, ProcesosEnum.BI_DISINFECTION_RESULTS);
        processResultsBiologicalIndicatorsOfDisinfection(responseBiologicalIndicatorsOfDisinfectionDTOList);

        /*Api Bi Protein*/
        String protein = getProResults(token.getToken(), new GenericDTO(from, to, statusRead));
        List<ResponseProteinIndicatorsDTO> responseProteinIndicatorsDTOList = filtrarMensaje(mapper, resultList, protein, ProcesosEnum.PROTEIN_INDICATORS_RESULTS);
        processResultsProteinIndicators(responseProteinIndicatorsDTOList);

        /*Api Bi Washer*/
        String wash = getWasherResults(token.getToken(), new GenericDTO(from, to, statusRead));
        List<ResponseWahingChemicalIndicatorsDTO> responseWahingChemicalIndicatorsDTOList = filtrarMensaje(mapper, resultList, wash, ProcesosEnum.WASHER_RESULTS);
         processWahingChemicalIndicators(responseWahingChemicalIndicatorsDTOList);

        /*Api Bi Sterilizer*/
        String sterilizer = getSterilizerResults(token.getToken(), new GenericDTO(from, to, statusRead));
        List<ResponseSterilizationChemicalIndicatorsDTO> responseSterilizationChemicalIndicatorsDTOList = filtrarMensaje(mapper, resultList, sterilizer, ProcesosEnum.STERILIZER_RESULTS);
        processResultsSterilizationChemicalIndicators(responseSterilizationChemicalIndicatorsDTOList);
    }

    private void processResultsSterilizationChemicalIndicators(List<ResponseSterilizationChemicalIndicatorsDTO> responseSterilizationChemicalIndicatorsDTOList) {
        if (!responseSterilizationChemicalIndicatorsDTOList.isEmpty()) {
            responseSterilizationChemicalIndicatorsDTOList.forEach(bi -> {
                    List<SterilizerEntity> sterilizerEntityList = sterilizerRepository.searchByCreationTest(bi.getData().getCreationTest());
                    if (sterilizerEntityList.isEmpty()) {
                        guardarEnBaseSterilizerResults(bi);
                    }
            });
        }
    }

    private void processResultsProteinIndicators(List<ResponseProteinIndicatorsDTO> responseProteinIndicatorsDTOList) {
        if (!responseProteinIndicatorsDTOList.isEmpty()) {
            responseProteinIndicatorsDTOList.forEach(bi -> {
                List<ProteinEntity> proteinEntityList = proteinRepository.searchByTicketNumber(bi.getData().getTicketNumber());
                if (proteinEntityList.isEmpty()) {
                    guardarEnBaseProteinResult(bi);
                }
            });
        }
    }

    private void processResultsBiologicalIndicatorsOfDisinfection(List<ResponseBiologicalIndicatorsOfDisinfectionDTO> responseBiologicalIndicatorsOfDisinfectionDTOList) {
        if (!responseBiologicalIndicatorsOfDisinfectionDTOList.isEmpty()) {
            responseBiologicalIndicatorsOfDisinfectionDTOList.forEach(bi -> {
                List<DisinfectionEntity> disinfectionEntityList = disinfectionRepository.searchByTicketNumber(bi.getData().getTicketNumber());
                if (disinfectionEntityList.isEmpty()) {
                    guardarEnBaseDisinfectionResults(bi);
                }
            });
        }
    }

    private void processResultsBiologicalIndicatorSterilization(List<ResponseBiologicalIndicatorSterilizationDTO> responseBiologicalIndicatorSterilizationDTOSList) {
        if (!responseBiologicalIndicatorSterilizationDTOSList.isEmpty()) {
            responseBiologicalIndicatorSterilizationDTOSList.forEach(bi -> {
                        List<SterilizationEntity> sterilizationEntityList = sterilizationRepository.searchByTicketNumber(bi.getData().getTicketNumber());
                        if (sterilizationEntityList.isEmpty()) {
                            guardarEnBaseSterilizationResult(bi);
                        }
                    }
            );
        }
    }

    private void processWahingChemicalIndicators(List<ResponseWahingChemicalIndicatorsDTO> responseWahingChemicalIndicatorsDTOList) {
        if (!responseWahingChemicalIndicatorsDTOList.isEmpty()) {
            responseWahingChemicalIndicatorsDTOList.forEach(bi -> {
                List<WasherEntity> washingEntityList = washingRepository.searchByCreationTest(bi.getData().getCreationTest());
                if (washingEntityList.isEmpty()) {
                    guardarEnBaseWashingResults(bi);
                }
            });
        }
    }

    private void guardarEnBaseWashingResults(ResponseWahingChemicalIndicatorsDTO bi) {
        WasherEntity washerEntity = new WasherEntity();
        washerEntity.setManufactureIndicator(getValueOrDefault(bi.getDevice().getManufactureIndicator()));
        washerEntity.setSerialWasher(getValueOrDefault(bi.getDevice().getSerialWasher()));
        washerEntity.setBrandWasher(getValueOrDefault(bi.getDevice().getBrandWasher()));
        washerEntity.setUserName(getValueOrDefault(bi.getDevice().getUserName()));
        washerEntity.setManufactureDate(getValueOrDefault(bi.getData().getManufactureDate()));
        washerEntity.setWashingTime(getValueOrDefault(bi.getData().getWashingTime()));
        washerEntity.setDetergentType(getValueOrDefault(bi.getData().getDetergentType()));
        washerEntity.setDetergentConcentration(getValueOrDefault(bi.getData().getDetergentConcentration()));
        washerEntity.setProgram(getValueOrDefault(bi.getData().getProgram()));
        washerEntity.setLocation(getValueOrDefault(bi.getData().getLocation()));
        washerEntity.setCreationTest(getValueOrDefault(bi.getData().getCreationTest()));
        washerEntity.setTemperature(getValueOrDefault(bi.getData().getTemperature()));
        washerEntity.setExpirationDate(getValueOrDefault(bi.getData().getExpirationDate()));
        washerEntity.setSerialNumberScanner(getValueOrDefault(bi.getData().getSerialNumberScanner()));
        washerEntity.setLotProduct(getValueOrDefault(bi.getData().getLotProduct()));
        washerEntity.setBrand(getValueOrDefault(bi.getData().getBrand()));
        washerEntity.setWaterPressure(getValueOrDefault(bi.getData().getWaterPressure()));
        washerEntity.setWaterHardness(getValueOrDefault(bi.getData().getWaterHardness()));
        washerEntity.setCycle(getValueOrDefault(bi.getData().getCycle()));
        washingRepository.save(washerEntity);
    }

    private void guardarEnBaseSterilizerResults(ResponseSterilizationChemicalIndicatorsDTO bi) {
        SterilizerEntity sterilizerEntity = new SterilizerEntity();
        sterilizerEntity.setManufactureIndicator(getValueOrDefault(bi.getDevice().getManufactureIndicator()));
        sterilizerEntity.setSerialSterilizer(getValueOrDefault(bi.getDevice().getSerialSterilizer()));
        sterilizerEntity.setBrandSterilizer(getValueOrDefault(bi.getDevice().getBrandSterilizer()));
        sterilizerEntity.setUserName(getValueOrDefault(bi.getDevice().getUserName()));
        sterilizerEntity.setManufactureDate(getValueOrDefault(bi.getData().getManufactureDate()));
        sterilizerEntity.setExposureTime(getValueOrDefault(bi.getData().getExposureTime()));
        sterilizerEntity.setResult(getValueOrDefault(bi.getData().getResult()));
        sterilizerEntity.setConcentration(getValueOrDefault(bi.getData().getConcentration()));
        sterilizerEntity.setRelativeDampness(getValueOrDefault(bi.getData().getRelativeDampness()));
        sterilizerEntity.setPackageNumber(getValueOrDefault(bi.getData().getPackageNumber()));
        sterilizerEntity.setProgram(getValueOrDefault(bi.getData().getProgram()));
        sterilizerEntity.setCreationTest(getValueOrDefault(bi.getData().getCreationTest()));
        sterilizerEntity.setTemperature(getValueOrDefault(bi.getData().getTemperature()));
        sterilizerEntity.setExpirationDate(getValueOrDefault(bi.getData().getExpirationDate()));
        sterilizerEntity.setSerialNumberScanner(getValueOrDefault(bi.getData().getSerialNumberScanner()));
        sterilizerEntity.setNameProduct(getValueOrDefault(bi.getData().getNameProduct()));
        sterilizerEntity.setLoteProduct(getValueOrDefault(bi.getData().getLoteProduct()));
        sterilizerEntity.setBrand(getValueOrDefault(bi.getData().getBrand()));
        sterilizerEntity.setCycle(getValueOrDefault(bi.getData().getCycle()));
        sterilizerRepository.save(sterilizerEntity);
    }

    private String getValueOrDefault(String value) {
        return !Util.validateIsNullOrEmpty(value) ? value : NO_DATA;
    }


    private void guardarEnBaseProteinResult(ResponseProteinIndicatorsDTO bi) {
        ProteinEntity proteinEntity = new ProteinEntity();
        proteinEntity.setPositionNumber(getValueOrDefault(bi.getDevice().getPositionNumber()));
        proteinEntity.setWasherSerial(getValueOrDefault(bi.getDevice().getWasherSerial()));
        proteinEntity.setWasherName(getValueOrDefault(bi.getDevice().getWasherName()));
        proteinEntity.setProtein(getValueOrDefault(bi.getDevice().getProtein()));
        proteinEntity.setUserName(getValueOrDefault(bi.getDevice().getUserName()));
        proteinEntity.setTicketNumber(getValueOrDefault(bi.getData().getTicketNumber()));
        proteinEntity.setResult(getValueOrDefault(bi.getData().getResult()));
        proteinEntity.setSurface(getValueOrDefault(bi.getData().getSurface()));
        proteinEntity.setProgramNumber(getValueOrDefault(bi.getData().getProgramNumber()));
        proteinEntity.setStartedTime(getValueOrDefault(bi.getData().getStartedTime()));
        proteinEntity.setAverageTemperature(getValueOrDefault(bi.getData().getAverageTemperature()));
        proteinEntity.setResultDate(getValueOrDefault(bi.getData().getResultDate()));
        proteinEntity.setIncubatorSerialNumber(getValueOrDefault(bi.getData().getIncubatorSerialNumber()));
        proteinEntity.setProductName(getValueOrDefault(bi.getData().getProductName()));
        proteinEntity.setLoteProduct(getValueOrDefault(bi.getData().getLoteProduct()));
        proteinEntity.setProductBrand(getValueOrDefault(bi.getData().getProductBrand()));
        proteinEntity.setIncubatorName(getValueOrDefault(bi.getData().getIncubatorName()));
        proteinEntity.setCycle(getValueOrDefault(bi.getData().getCycle()));
        proteinRepository.save(proteinEntity);

    }

    private void guardarEnBaseDisinfectionResults(ResponseBiologicalIndicatorsOfDisinfectionDTO bi) {
        DisinfectionEntity disinfectionEntity = new DisinfectionEntity();

        disinfectionEntity.setTicketNumber(getValueOrDefault(bi.getData().getTicketNumber()));
        disinfectionEntity.setStartedTime(getValueOrDefault(bi.getData().getStartedTime()));
        disinfectionEntity.setResult(getValueOrDefault(bi.getData().getResult()));
        disinfectionEntity.setProcess(getValueOrDefault(bi.getData().getProcess()));
        disinfectionEntity.setConditionSCIB(getValueOrDefault(bi.getData().getConditionSCIB()));
        disinfectionEntity.setRoomId(getValueOrDefault(bi.getData().getRoomId()));
        disinfectionEntity.setRoomVolume(getValueOrDefault(bi.getData().getRoomVolume()));
        disinfectionEntity.setAverageTemperature(getValueOrDefault(bi.getData().getAverageTemperature()));
        disinfectionEntity.setResultDate(getValueOrDefault(bi.getData().getResultDate()));
        disinfectionEntity.setIncubatorSerialNumber(getValueOrDefault(bi.getData().getIncubatorSerialNumber()));
        disinfectionEntity.setProductName(getValueOrDefault(bi.getData().getProductName()));
        disinfectionEntity.setProductLot(getValueOrDefault(bi.getData().getProductLot()));
        disinfectionEntity.setProductBrand(getValueOrDefault(bi.getData().getProductBrand()));
        disinfectionEntity.setIncubatorName(getValueOrDefault(bi.getData().getIncubatorName()));
        disinfectionEntity.setDisinfectorSerial(getValueOrDefault(bi.getDevice().getDisinfectorSerial()));
        disinfectionEntity.setDisinfectorName(getValueOrDefault(bi.getDevice().getDisinfectorName()));
        disinfectionEntity.setPeroxideConcentration(getValueOrDefault(bi.getDevice().getPeroxideConcentration()));
        disinfectionEntity.setUserName(getValueOrDefault(bi.getDevice().getUserName()));
        disinfectionEntity.setPositionNumber(getValueOrDefault(bi.getDevice().getPositionNumber()));
        disinfectionRepository.save(disinfectionEntity);
    }

    private void guardarEnBaseSterilizationResult(ResponseBiologicalIndicatorSterilizationDTO bi) {
        SterilizationEntity resultSterilization = new SterilizationEntity();
        resultSterilization.setTicketNumber(getValueOrDefault(bi.getData().getTicketNumber()));
        resultSterilization.setProcess(getValueOrDefault(bi.getData().getProcess()));
        resultSterilization.setConditionSCIB(getValueOrDefault(bi.getData().getConditionSCIB()));
        resultSterilization.setLoadNumber(getValueOrDefault(bi.getData().getLoadNumber()));
        resultSterilization.setProgramNumber(getValueOrDefault(bi.getData().getProgramNumber()));
        resultSterilization.setStartedTime(getValueOrDefault(bi.getData().getStartedTime()));
        resultSterilization.setAverageTemperature(getValueOrDefault(bi.getData().getAverageTemperature()));
        resultSterilization.setResultDate(getValueOrDefault(bi.getData().getResultDate()));
        resultSterilization.setIncubatorSerialNumber(getValueOrDefault(bi.getData().getIncubatorSerialNumber()));
        resultSterilization.setProductName(getValueOrDefault(bi.getData().getProductName()));
        resultSterilization.setProductLot(getValueOrDefault(bi.getData().getProductLot()));
        resultSterilization.setProductBrand(getValueOrDefault(bi.getData().getProductBrand()));
        resultSterilization.setIncubatorName(getValueOrDefault(bi.getData().getIncubatorName()));
        resultSterilization.setCycle(getValueOrDefault(bi.getData().getCycle()));
        resultSterilization.setResult(getValueOrDefault(bi.getData().getResult()));
        resultSterilization.setPositionNumber(getValueOrDefault(bi.getDevice().getPositionNumber()));
        resultSterilization.setSterilizerSerial(getValueOrDefault(bi.getDevice().getSterilizerSerial()));
        resultSterilization.setSterilizerName(getValueOrDefault(bi.getDevice().getSterilizerName()));
        resultSterilization.setDValue(getValueOrDefault(bi.getDevice().getDValue()));
        resultSterilization.setUserName(getValueOrDefault(bi.getDevice().getUserName()));
        sterilizationRepository.save(resultSterilization);
    }


    /**
     * Se filtra de la lista los que coincidan con el criterio de busqueda
     *
     * @param mapper     mapper
     * @param resultList lista de resultados
     * @param json       json
     * @param proceso    proceso
     * @param <T>        tipo de dato
     * @throws JsonProcessingException excepcion
     */
    private <T> List<T> filtrarMensaje(ObjectMapper mapper, List<Map<String, Object>> resultList, String json, ProcesosEnum proceso) throws JsonProcessingException {
        List<Map<String, Object>> jsonList = mapper.readValue(json, new TypeReference<>() {
        });

        resultList.addAll(jsonList);

        switch (proceso) {
            case BI_STERILIZATION_RESULTS -> {
                List<ResponseBiologicalIndicatorSterilizationDTO> listaDatosSterilization = new ArrayList<>();
                for (Map<String, Object> map : resultList) {
                    ResponseBiologicalIndicatorSterilizationDTO responseBiologicalIndicatorSterilizationDTO = new ResponseBiologicalIndicatorSterilizationDTO();
                    BiologicalIndicatorSterilizationDataDeviceDTO biologicalIndicatorSterilizationDataDeviceDTO = mapper.convertValue(map.get(DATA_DEVICE), BiologicalIndicatorSterilizationDataDeviceDTO.class);
                    BiologicalIndicatorSterilizationDataDTO biologicalIndicatorSterilizationDatosDTO = mapper.convertValue(map.get(DATA), BiologicalIndicatorSterilizationDataDTO.class);
                    responseBiologicalIndicatorSterilizationDTO.setDevice(biologicalIndicatorSterilizationDataDeviceDTO);
                    responseBiologicalIndicatorSterilizationDTO.setData(biologicalIndicatorSterilizationDatosDTO);
                    listaDatosSterilization.add(responseBiologicalIndicatorSterilizationDTO);
                }
                resultList.clear();
                return (List<T>) listaDatosSterilization;
            }
            case BI_DISINFECTION_RESULTS -> {
                List<ResponseBiologicalIndicatorsOfDisinfectionDTO> listaDatosDisinfection = new ArrayList<>();
                for (Map<String, Object> map : resultList) {
                    ResponseBiologicalIndicatorsOfDisinfectionDTO responseBiologicalIndicatorsOfDisinfectionDTO = new ResponseBiologicalIndicatorsOfDisinfectionDTO();
                    BiologicalIndicatorsOfDisinfectionDataDeviceDTO biologicalIndicatorsOfDisinfectionDataDeviceDTO = mapper.convertValue(map.get(DATA_DEVICE), BiologicalIndicatorsOfDisinfectionDataDeviceDTO.class);
                    BiologicalIndicatorsOfDisinfectionDataDTO biologicalIndicatorsOfDisinfectionDataDTO = mapper.convertValue(map.get(DATA), BiologicalIndicatorsOfDisinfectionDataDTO.class);
                    responseBiologicalIndicatorsOfDisinfectionDTO.setDevice(biologicalIndicatorsOfDisinfectionDataDeviceDTO);
                    responseBiologicalIndicatorsOfDisinfectionDTO.setData(biologicalIndicatorsOfDisinfectionDataDTO);
                    listaDatosDisinfection.add(responseBiologicalIndicatorsOfDisinfectionDTO);
                }
                resultList.clear();
                return (List<T>) listaDatosDisinfection;
            }
            case PROTEIN_INDICATORS_RESULTS -> {
                List<ResponseProteinIndicatorsDTO> listaDatosProtein = new ArrayList<>();
                for (Map<String, Object> map : resultList) {
                    ResponseProteinIndicatorsDTO responseProteinIndicatorsDTO = new ResponseProteinIndicatorsDTO();
                    ProteinIndicatorsDataDeviceDTO proteinIndicatorsDataDeviceDTO = mapper.convertValue(map.get(DATA_DEVICE), ProteinIndicatorsDataDeviceDTO.class);
                    ProteinIndicatorsDataDTO proteinIndicatorsDataDTO = mapper.convertValue(map.get(DATA), ProteinIndicatorsDataDTO.class);
                    responseProteinIndicatorsDTO.setDevice(proteinIndicatorsDataDeviceDTO);
                    responseProteinIndicatorsDTO.setData(proteinIndicatorsDataDTO);
                    listaDatosProtein.add(responseProteinIndicatorsDTO);
                }
                resultList.clear();
                return (List<T>) listaDatosProtein;
            }
            case WASHER_RESULTS -> {
                List<ResponseWahingChemicalIndicatorsDTO> listaDatosWasher = new ArrayList<>();
                for (Map<String, Object> map : resultList) {
                    ResponseWahingChemicalIndicatorsDTO responseWahingChemicalIndicatorsDTO = new ResponseWahingChemicalIndicatorsDTO();
                    WashingChemicalIndicatorsDataDeviceDTO washingChemicalIndicatorsDataDeviceDTO = mapper.convertValue(map.get(DATA_DEVICE), WashingChemicalIndicatorsDataDeviceDTO.class);
                    WashingChemicalIndicatorsDataDTO washingChemicalIndicatorsDataDTO = mapper.convertValue(map.get(DATA), WashingChemicalIndicatorsDataDTO.class);
                    responseWahingChemicalIndicatorsDTO.setDevice(washingChemicalIndicatorsDataDeviceDTO);
                    responseWahingChemicalIndicatorsDTO.setData(washingChemicalIndicatorsDataDTO);
                    listaDatosWasher.add(responseWahingChemicalIndicatorsDTO);
                }
                resultList.clear();
                return (List<T>) listaDatosWasher;
            }
            case STERILIZER_RESULTS -> {
                List<ResponseSterilizationChemicalIndicatorsDTO> listaDatosSterilizer = new ArrayList<>();
                for (Map<String, Object> map : jsonList) {
                    ResponseSterilizationChemicalIndicatorsDTO responseSterilizationChemicalIndicatorsDTO = new ResponseSterilizationChemicalIndicatorsDTO();
                    SterilizationChemicalIndicatorsDataDeviceDTO sterilizationChemicalIndicatorsDataDeviceDTO = mapper.convertValue(map.get(DATA_DEVICE), SterilizationChemicalIndicatorsDataDeviceDTO.class);
                    SterilizationChemicalIndicatorsDataDTO sterilizationChemicalIndicatorsDataDTO = mapper.convertValue(map.get(DATA), SterilizationChemicalIndicatorsDataDTO.class);
                    responseSterilizationChemicalIndicatorsDTO.setDevice(sterilizationChemicalIndicatorsDataDeviceDTO);
                    responseSterilizationChemicalIndicatorsDTO.setData(sterilizationChemicalIndicatorsDataDTO);
                    listaDatosSterilizer.add(responseSterilizationChemicalIndicatorsDTO);
                }
                resultList.clear();
                return (List<T>) listaDatosSterilizer;
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean matchesCriteria(Map<String, Object> object, String position, String incubatorSerialNumber) {
        Map<String, Object> dataDevice = (Map<String, Object>) object.get(DATA_DEVICE);
        Map<String, Object> data = (Map<String, Object>) object.get(DATA);

        // Verificar si los campos "positionNumber" y "ticketNumber" son los que necesitas
        return (position.equals(dataDevice.get(POSITION_NUMBER)) && incubatorSerialNumber.equals(data.get(INCUBATOR_SERIAL_NUMBER)));
    }


    /**
     * Se obtiene el token para poder hacer el llamado a los endpoints
     *
     * @param login
     * @return
     */
    public TokenDTO tokenLogin(LoginDTO login) {
        TokenDTO token = new TokenDTO();
        try {
            token.setToken(restTemplate.postForObject(basePath + "/login/login", login, String.class));
        } catch (Exception e) {
            throw new ObtenerTokenException("Error al obtener el token" + e.getMessage());
        }

        return token;
    }

    public String getBiSterilizationResult(String token, GenericDTO genericDTO) throws ParseException {
        List<ResponseBiologicalIndicatorSterilizationDTO> listaDatos = new ArrayList<>();
        String cadena = genericGetWithBodyResult(token, genericDTO, "/Reading/GetBiSterilizationResults");

        if (cadena != null) {

            List<String> datosDispositivo = resultadoDatosDispositivoYLectura(cadena).get(RESULTADO_DATOS_DISP);

            List<String> datosLectura = resultadoDatosDispositivoYLectura(cadena).get(RESULTADO_DATOS_LECTURA);

            Iterator<String> iterator1 = datosDispositivo.iterator();
            Iterator<String> iterator2 = datosLectura.iterator();

            while (iterator1.hasNext() && iterator2.hasNext()) {
                Map<String, String> datosLecturaMap = new HashMap<>();

                String[] cabecera = iterator1.next().split("\\|");
                String[] lecturaDatos = iterator2.next().split("\\|");

                procesarDatosDispositivo(cabecera, datosLecturaMap, ProcesosEnum.BI_STERILIZATION_RESULTS);
                procesarDatosLectura(lecturaDatos, datosLecturaMap, ProcesosEnum.BI_STERILIZATION_RESULTS);

                BiologicalIndicatorSterilizationDataDTO biologicalIndicatorSterilizationDatosDTO = new BiologicalIndicatorSterilizationDataDTO();
                BiologicalIndicatorSterilizationDataDeviceDTO biologicalIndicatorSterilizationDataDeviceDTO = new BiologicalIndicatorSterilizationDataDeviceDTO();
                ResponseBiologicalIndicatorSterilizationDTO responseBiologicalIndicatorSterilizationDTO = new ResponseBiologicalIndicatorSterilizationDTO();
                // Datos del dispositivo
                biologicalIndicatorSterilizationDataDeviceDTO.setPositionNumber(datosLecturaMap.get(POSITION_NUMBER));
                biologicalIndicatorSterilizationDataDeviceDTO.setSterilizerSerial(datosLecturaMap.get(STERILIZER_SERIAL));
                biologicalIndicatorSterilizationDataDeviceDTO.setSterilizerName(datosLecturaMap.get(STERILIZER_NAME));
                biologicalIndicatorSterilizationDataDeviceDTO.setDValue(datosLecturaMap.get(D_VALUE));
                biologicalIndicatorSterilizationDataDeviceDTO.setUserName(datosLecturaMap.get(USER_NAME));

                // Datos de la lectura
                biologicalIndicatorSterilizationDatosDTO.setTicketNumber(datosLecturaMap.get(TICKET_NUMBER));
                biologicalIndicatorSterilizationDatosDTO.setResult(datosLecturaMap.get(RESULT));
                biologicalIndicatorSterilizationDatosDTO.setProcess(datosLecturaMap.get(PROCESS));
                biologicalIndicatorSterilizationDatosDTO.setConditionSCIB(datosLecturaMap.get(CONDITION_SCIB));
                biologicalIndicatorSterilizationDatosDTO.setLoadNumber(datosLecturaMap.get(LOAD_NUMBER));
                biologicalIndicatorSterilizationDatosDTO.setProgramNumber(datosLecturaMap.get(PROGRAM_NUMBER));
                biologicalIndicatorSterilizationDatosDTO.setStartedTime(datosLecturaMap.get(STARTED_TIME));
                biologicalIndicatorSterilizationDatosDTO.setAverageTemperature(datosLecturaMap.get(AVERAGE_TEMPERATURE));
                biologicalIndicatorSterilizationDatosDTO.setResultDate(datosLecturaMap.get(RESULT_DATE));
                biologicalIndicatorSterilizationDatosDTO.setIncubatorSerialNumber(datosLecturaMap.get(INCUBATOR_SERIAL_NUMBER));
                biologicalIndicatorSterilizationDatosDTO.setProductName(datosLecturaMap.get(PRODUCT_NAME));
                biologicalIndicatorSterilizationDatosDTO.setProductLot(datosLecturaMap.get(PRODUCT_LOT));
                biologicalIndicatorSterilizationDatosDTO.setProductBrand(datosLecturaMap.get(PRODUCT_BRAND));
                biologicalIndicatorSterilizationDatosDTO.setIncubatorName(datosLecturaMap.get(INCUBATOR_NAME));
                biologicalIndicatorSterilizationDatosDTO.setCycle(datosLecturaMap.get(CYCLE));

                responseBiologicalIndicatorSterilizationDTO.setDevice(biologicalIndicatorSterilizationDataDeviceDTO);
                responseBiologicalIndicatorSterilizationDTO.setData(biologicalIndicatorSterilizationDatosDTO);

                listaDatos.add(responseBiologicalIndicatorSterilizationDTO);
            }
        }

        return convert(listaDatos);
    }

    public String getBiDisinfectionResults(String token, GenericDTO genericDTO) throws ParseException {
        List<ResponseBiologicalIndicatorsOfDisinfectionDTO> listaDatos = new ArrayList<>();
        String cadena = genericGetWithBodyResult(token, genericDTO, "/Reading/GetBiDisinfectionResults");

        if (cadena != null) {

            List<String> datosDispositivo = resultadoDatosDispositivoYLectura(cadena).get(RESULTADO_DATOS_DISP);
            List<String> datosLectura = resultadoDatosDispositivoYLectura(cadena).get(RESULTADO_DATOS_LECTURA);

            Iterator<String> iterator1 = datosDispositivo.iterator();
            Iterator<String> iterator2 = datosLectura.iterator();

            while (iterator1.hasNext() && iterator2.hasNext()) {
                Map<String, String> datosLecturaMap = new HashMap<>();

                String[] cabecera = iterator1.next().split("\\|");
                String[] lecturaDatos = iterator2.next().split("\\|");

                procesarDatosDispositivo(cabecera, datosLecturaMap, ProcesosEnum.BI_DISINFECTION_RESULTS);
                procesarDatosLectura(lecturaDatos, datosLecturaMap, ProcesosEnum.BI_DISINFECTION_RESULTS);

                BiologicalIndicatorsOfDisinfectionDataDTO biologicalIndicatorsOfDisinfectionDataDTO = new BiologicalIndicatorsOfDisinfectionDataDTO();
                BiologicalIndicatorsOfDisinfectionDataDeviceDTO biologicalIndicatorsOfDisinfectionDataDeviceDTO = new BiologicalIndicatorsOfDisinfectionDataDeviceDTO();
                ResponseBiologicalIndicatorsOfDisinfectionDTO responseBiologicalIndicatorsOfDisinfectionDTO = new ResponseBiologicalIndicatorsOfDisinfectionDTO();

                // Datos del dispositivo
                biologicalIndicatorsOfDisinfectionDataDeviceDTO.setPositionNumber(datosLecturaMap.get(POSITION_NUMBER));
                biologicalIndicatorsOfDisinfectionDataDeviceDTO.setDisinfectorSerial(datosLecturaMap.get(DISINFECTOR_SERIAL));
                biologicalIndicatorsOfDisinfectionDataDeviceDTO.setDisinfectorName(datosLecturaMap.get(DISINFECTOR_NAME));
                biologicalIndicatorsOfDisinfectionDataDeviceDTO.setPeroxideConcentration(datosLecturaMap.get(PEROXIDE_CONCENTRATION));
                biologicalIndicatorsOfDisinfectionDataDeviceDTO.setUserName(datosLecturaMap.get(USER_NAME));

                // Datos de la lectura

                biologicalIndicatorsOfDisinfectionDataDTO.setTicketNumber(datosLecturaMap.get(TICKET_NUMBER));
                biologicalIndicatorsOfDisinfectionDataDTO.setResult(datosLecturaMap.get(RESULT));
                biologicalIndicatorsOfDisinfectionDataDTO.setProcess(datosLecturaMap.get(PROCESS));
                biologicalIndicatorsOfDisinfectionDataDTO.setConditionSCIB(datosLecturaMap.get(CONDITION_SCIB));
                biologicalIndicatorsOfDisinfectionDataDTO.setRoomId(datosLecturaMap.get(ROOM_ID));
                biologicalIndicatorsOfDisinfectionDataDTO.setRoomVolume(datosLecturaMap.get(ROOM_VOLUME));
                biologicalIndicatorsOfDisinfectionDataDTO.setStartedTime(datosLecturaMap.get(STARTED_TIME));
                biologicalIndicatorsOfDisinfectionDataDTO.setAverageTemperature(datosLecturaMap.get(AVERAGE_TEMPERATURE));
                biologicalIndicatorsOfDisinfectionDataDTO.setResultDate(datosLecturaMap.get(RESULT_DATE));
                biologicalIndicatorsOfDisinfectionDataDTO.setIncubatorSerialNumber(datosLecturaMap.get(INCUBATOR_SERIAL_NUMBER));
                biologicalIndicatorsOfDisinfectionDataDTO.setProductName(datosLecturaMap.get(PRODUCT_NAME));
                biologicalIndicatorsOfDisinfectionDataDTO.setProductLot(datosLecturaMap.get(PRODUCT_LOT));
                biologicalIndicatorsOfDisinfectionDataDTO.setProductBrand(datosLecturaMap.get(PRODUCT_BRAND));
                biologicalIndicatorsOfDisinfectionDataDTO.setIncubatorName(datosLecturaMap.get(INCUBATOR_NAME));

                responseBiologicalIndicatorsOfDisinfectionDTO.setDevice(biologicalIndicatorsOfDisinfectionDataDeviceDTO);
                responseBiologicalIndicatorsOfDisinfectionDTO.setData(biologicalIndicatorsOfDisinfectionDataDTO);

                listaDatos.add(responseBiologicalIndicatorsOfDisinfectionDTO);
            }
        }
        return convert(listaDatos);
    }

    public String getProResults(String token, GenericDTO genericDTO) throws ParseException {
        List<ResponseProteinIndicatorsDTO> listaDatos = new ArrayList<>();
        String cadena = genericGetWithBodyResult(token, genericDTO, "/Reading/GetProResults");

        if (cadena != null) {

            List<String> datosDispositivo = resultadoDatosDispositivoYLectura(cadena).get(RESULTADO_DATOS_DISP);
            List<String> datosLectura = resultadoDatosDispositivoYLectura(cadena).get(RESULTADO_DATOS_LECTURA);

            Iterator<String> iterator1 = datosDispositivo.iterator();
            Iterator<String> iterator2 = datosLectura.iterator();

            while (iterator1.hasNext() && iterator2.hasNext()) {
                Map<String, String> datosLecturaMap = new HashMap<>();

                String[] cabecera = iterator1.next().split("\\|");
                String[] lecturaDatos = iterator2.next().split("\\|");

                procesarDatosDispositivo(cabecera, datosLecturaMap, ProcesosEnum.PROTEIN_INDICATORS_RESULTS);
                procesarDatosLectura(lecturaDatos, datosLecturaMap, ProcesosEnum.PROTEIN_INDICATORS_RESULTS);

                ProteinIndicatorsDataDTO proteinIndicatorsDataDTO = new ProteinIndicatorsDataDTO();
                ProteinIndicatorsDataDeviceDTO proteinIndicatorsDataDeviceDTO = new ProteinIndicatorsDataDeviceDTO();
                ResponseProteinIndicatorsDTO responseProteinIndicatorsDTO = new ResponseProteinIndicatorsDTO();

                // Datos del dispositivo
                proteinIndicatorsDataDeviceDTO.setPositionNumber(datosLecturaMap.get(POSITION_NUMBER));
                proteinIndicatorsDataDeviceDTO.setWasherName(datosLecturaMap.get(WASHER));
                proteinIndicatorsDataDeviceDTO.setWasherSerial(datosLecturaMap.get(WASHER_SERIAL));
                proteinIndicatorsDataDeviceDTO.setUserName(datosLecturaMap.get(USER_NAME));
                proteinIndicatorsDataDeviceDTO.setProtein(datosLecturaMap.get(NAME_PROTEIN));

                // Datos de la lectura

                proteinIndicatorsDataDTO.setTicketNumber(datosLecturaMap.get(TICKET_NUMBER));
                proteinIndicatorsDataDTO.setResult(datosLecturaMap.get(RESULT));
                proteinIndicatorsDataDTO.setSurface(datosLecturaMap.get(SURFACE));
                proteinIndicatorsDataDTO.setProgramNumber(datosLecturaMap.get(PROGRAM_NUMBER));
                proteinIndicatorsDataDTO.setStartedTime(datosLecturaMap.get(STARTED_TIME));
                proteinIndicatorsDataDTO.setAverageTemperature(datosLecturaMap.get(AVERAGE_TEMPERATURE));
                proteinIndicatorsDataDTO.setResultDate(datosLecturaMap.get(RESULT_DATE));
                proteinIndicatorsDataDTO.setIncubatorSerialNumber(datosLecturaMap.get(INCUBATOR_SERIAL_NUMBER));
                proteinIndicatorsDataDTO.setProductName(datosLecturaMap.get(PRODUCT_NAME));
                proteinIndicatorsDataDTO.setLoteProduct(datosLecturaMap.get(LOTE_PRODUCT));
                proteinIndicatorsDataDTO.setProductBrand(datosLecturaMap.get(PRODUCT_BRAND));
                proteinIndicatorsDataDTO.setIncubatorName(datosLecturaMap.get(INCUBATOR_NAME));
                proteinIndicatorsDataDTO.setCycle(datosLecturaMap.get(CYCLE));

                // Datos de la respuesta
                responseProteinIndicatorsDTO.setDevice(proteinIndicatorsDataDeviceDTO);
                responseProteinIndicatorsDTO.setData(proteinIndicatorsDataDTO);

                listaDatos.add(responseProteinIndicatorsDTO);
            }
        }


        return convert(listaDatos);
    }

    public String getWasherResults(String token, GenericDTO genericDTO) throws ParseException {
        List<ResponseWahingChemicalIndicatorsDTO> listaDatos = new ArrayList<>();
        String cadena = genericGetWithBodyResult(token, genericDTO, "/Reading/GetWasherResults");

        if (cadena != null) {

            List<String> datosDispositivo = resultadoDatosDispositivoYLectura(cadena).get(RESULTADO_DATOS_DISP);
            List<String> datosLectura = resultadoDatosDispositivoYLectura(cadena).get(RESULTADO_DATOS_LECTURA);

            Iterator<String> iterator1 = datosDispositivo.iterator();
            Iterator<String> iterator2 = datosLectura.iterator();

            while (iterator1.hasNext() && iterator2.hasNext()) {
                Map<String, String> datosLecturaMap = new HashMap<>();

                String[] cabecera = iterator1.next().split("\\|");
                String[] lecturaDatos = iterator2.next().split("\\|");

                procesarDatosDispositivo(cabecera, datosLecturaMap, ProcesosEnum.WASHER_RESULTS);
                procesarDatosLectura(lecturaDatos, datosLecturaMap, ProcesosEnum.WASHER_RESULTS);

                WashingChemicalIndicatorsDataDTO wahingChemicalIndicatorsDataDTO = new WashingChemicalIndicatorsDataDTO();
                WashingChemicalIndicatorsDataDeviceDTO washingChemicalIndicatorsDataDeviceDTO = new WashingChemicalIndicatorsDataDeviceDTO();
                ResponseWahingChemicalIndicatorsDTO responseWahingChemicalIndicatorsDTO = new ResponseWahingChemicalIndicatorsDTO();

                // Datos del dispositivo

                washingChemicalIndicatorsDataDeviceDTO.setManufactureIndicator(datosLecturaMap.get(MANUFACTURE_INDICATOR));
                washingChemicalIndicatorsDataDeviceDTO.setSerialWasher(datosLecturaMap.get(SERIAL_WASHER));
                washingChemicalIndicatorsDataDeviceDTO.setBrandWasher(datosLecturaMap.get(BRAND_WASHER));
                washingChemicalIndicatorsDataDeviceDTO.setUserName(datosLecturaMap.get(USER_NAME));

                // Datos de la lectura

                wahingChemicalIndicatorsDataDTO.setManufactureDate(datosLecturaMap.get(MANUFACTURE_DATE));
                wahingChemicalIndicatorsDataDTO.setWashingTime(datosLecturaMap.get(WASHING_TIME));
                wahingChemicalIndicatorsDataDTO.setResult(datosLecturaMap.get(RESULT));
                wahingChemicalIndicatorsDataDTO.setDetergentType(datosLecturaMap.get(DETERGENT_TYPE));
                wahingChemicalIndicatorsDataDTO.setDetergentConcentration(datosLecturaMap.get(DETERGENT_CONCENTRATION));
                wahingChemicalIndicatorsDataDTO.setProgram(datosLecturaMap.get(PROGRAM));
                wahingChemicalIndicatorsDataDTO.setLocation(datosLecturaMap.get(LOCATION));
                wahingChemicalIndicatorsDataDTO.setCreationTest(datosLecturaMap.get(CREATION_TEST));
                wahingChemicalIndicatorsDataDTO.setTemperature(datosLecturaMap.get(TEMPERATURE));
                wahingChemicalIndicatorsDataDTO.setExpirationDate(datosLecturaMap.get(EXPIRATION_DATE));
                wahingChemicalIndicatorsDataDTO.setSerialNumberScanner(datosLecturaMap.get(SERIAL_NUMBER_SCANNER));
                wahingChemicalIndicatorsDataDTO.setProductName(datosLecturaMap.get(PRODUCT_NAME));
                wahingChemicalIndicatorsDataDTO.setLotProduct(datosLecturaMap.get(LOT_PRODUCT));
                wahingChemicalIndicatorsDataDTO.setBrand(datosLecturaMap.get(BRAND));
                wahingChemicalIndicatorsDataDTO.setCycle(datosLecturaMap.get(CYCLE));
                wahingChemicalIndicatorsDataDTO.setWaterPressure(datosLecturaMap.get(WATER_PRESSURE));
                wahingChemicalIndicatorsDataDTO.setWaterHardness(datosLecturaMap.get(WATER_HARDNESS));

                // Datos de la respuesta
                responseWahingChemicalIndicatorsDTO.setDevice(washingChemicalIndicatorsDataDeviceDTO);
                responseWahingChemicalIndicatorsDTO.setData(wahingChemicalIndicatorsDataDTO);

                listaDatos.add(responseWahingChemicalIndicatorsDTO);
            }
        }


        return convert(listaDatos);
    }

    public String getSterilizerResults(String token, GenericDTO genericDTO) throws ParseException {
        List<ResponseSterilizationChemicalIndicatorsDTO> listaDatos = new ArrayList<>();
        String cadena = genericGetWithBodyResult(token, genericDTO, "/Reading/GetSterilizerResults");
        if (cadena != null) {

            List<String> datosDispositivo = resultadoDatosDispositivoYLectura(cadena).get(RESULTADO_DATOS_DISP);
            List<String> datosLectura = resultadoDatosDispositivoYLectura(cadena).get(RESULTADO_DATOS_LECTURA);

            Iterator<String> iterator1 = datosDispositivo.iterator();
            Iterator<String> iterator2 = datosLectura.iterator();

            while (iterator1.hasNext() && iterator2.hasNext()) {
                Map<String, String> datosLecturaMap = new HashMap<>();

                String[] cabecera = iterator1.next().split("\\|");
                String[] lecturaDatos = iterator2.next().split("\\|");

                procesarDatosDispositivo(cabecera, datosLecturaMap, ProcesosEnum.STERILIZER_RESULTS);
                procesarDatosLectura(lecturaDatos, datosLecturaMap, ProcesosEnum.STERILIZER_RESULTS);

                SterilizationChemicalIndicatorsDataDTO sterilizationChemicalIndicatorsDataDTO = new SterilizationChemicalIndicatorsDataDTO();
                SterilizationChemicalIndicatorsDataDeviceDTO sterilizationChemicalIndicatorsDataDeviceDTO = new SterilizationChemicalIndicatorsDataDeviceDTO();
                ResponseSterilizationChemicalIndicatorsDTO responseSterilizationChemicalIndicatorsDTO = new ResponseSterilizationChemicalIndicatorsDTO();

                // Datos del dispositivo

                sterilizationChemicalIndicatorsDataDeviceDTO.setManufactureIndicator(datosLecturaMap.get(MANUFACTURE_INDICATOR));
                sterilizationChemicalIndicatorsDataDeviceDTO.setSerialSterilizer(datosLecturaMap.get(SERIAL_STERILIZER));
                sterilizationChemicalIndicatorsDataDeviceDTO.setBrandSterilizer(datosLecturaMap.get(BRAND_STERILIZER));
                sterilizationChemicalIndicatorsDataDeviceDTO.setUserName(datosLecturaMap.get(USER_NAME));

                // Datos de la lectura

                sterilizationChemicalIndicatorsDataDTO.setManufactureDate(datosLecturaMap.get(MANUFACTURE_DATE));
                sterilizationChemicalIndicatorsDataDTO.setExposureTime(datosLecturaMap.get(EXPOSURE_TIME));
                sterilizationChemicalIndicatorsDataDTO.setResult(datosLecturaMap.get(RESULT));
                sterilizationChemicalIndicatorsDataDTO.setConcentration(datosLecturaMap.get(CONCENTRATION));
                sterilizationChemicalIndicatorsDataDTO.setRelativeDampness(datosLecturaMap.get(RELATIVE_DAMPNESS));
                sterilizationChemicalIndicatorsDataDTO.setPackageNumber(datosLecturaMap.get(PACKAGE_NUMBER));
                sterilizationChemicalIndicatorsDataDTO.setProgram(datosLecturaMap.get(PROGRAM));
                sterilizationChemicalIndicatorsDataDTO.setCreationTest(datosLecturaMap.get(CREATION_TEST));
                sterilizationChemicalIndicatorsDataDTO.setTemperature(datosLecturaMap.get(TEMPERATURE));
                sterilizationChemicalIndicatorsDataDTO.setExpirationDate(datosLecturaMap.get(EXPIRATION_DATE));
                sterilizationChemicalIndicatorsDataDTO.setSerialNumberScanner(datosLecturaMap.get(SERIAL_NUMBER_SCANNER));
                sterilizationChemicalIndicatorsDataDTO.setNameProduct(datosLecturaMap.get(NAME_PRODUCT));
                sterilizationChemicalIndicatorsDataDTO.setLoteProduct(datosLecturaMap.get(LOTE_PRODUCT));
                sterilizationChemicalIndicatorsDataDTO.setBrand(datosLecturaMap.get(BRAND));
                sterilizationChemicalIndicatorsDataDTO.setCycle(datosLecturaMap.get(CYCLE));

                // Datos de la respuesta
                responseSterilizationChemicalIndicatorsDTO.setDevice(sterilizationChemicalIndicatorsDataDeviceDTO);
                responseSterilizationChemicalIndicatorsDTO.setData(sterilizationChemicalIndicatorsDataDTO);


                listaDatos.add(responseSterilizationChemicalIndicatorsDTO);
            }
        }


        return convert(listaDatos);
    }

    /**
     * Método que prepara un Map con los datos del dispositivo
     *
     * @param cabecera
     * @param datosLecturaMap
     * @param processEnum
     * @return
     */
    private Map<String, String> procesarDatosDispositivo(String[] cabecera, Map<String, String> datosLecturaMap, ProcesosEnum processEnum) {
        switch (processEnum) {
            case BI_STERILIZATION_RESULTS -> {
                datosLecturaMap.put(POSITION_NUMBER, cabecera[1]);
                datosLecturaMap.put(STERILIZER_SERIAL, cabecera[2]);
                datosLecturaMap.put(STERILIZER_NAME, cabecera[3]);
                datosLecturaMap.put(D_VALUE, cabecera[6]);
                datosLecturaMap.put(USER_NAME, cabecera[7]);
            }
            case BI_DISINFECTION_RESULTS -> {
                datosLecturaMap.put(POSITION_NUMBER, cabecera[1]);
                datosLecturaMap.put(DISINFECTOR_SERIAL, cabecera[2]);
                datosLecturaMap.put(DISINFECTOR_NAME, cabecera[3]);
                datosLecturaMap.put(PEROXIDE_CONCENTRATION, cabecera[6]);
                datosLecturaMap.put(USER_NAME, cabecera[7]);
            }
            case PROTEIN_INDICATORS_RESULTS -> {
                /*
                 * SDD-1.1 Position Number
                 * SDD-2.1  Washer Serial
                 * SDD-3 Washer
                 * SDD-6 Name Protein
                 * SDD-7 UserName, UserlastName
                 */

                datosLecturaMap.put(POSITION_NUMBER, cabecera[1]);
                datosLecturaMap.put(WASHER_SERIAL, cabecera[2]);
                datosLecturaMap.put(WASHER, cabecera[3]);
                datosLecturaMap.put(NAME_PROTEIN, cabecera[6]);
                datosLecturaMap.put(USER_NAME, cabecera[7]);
            }
            case WASHER_RESULTS -> {
                /*
                 * SDD-1.1 Manufacture lndicator
                 * SDD-2.1 Serial Washer
                 * SDD-3   Brand Washer
                 * SDD-7 UserName, UserlastName
                 */

                datosLecturaMap.put(MANUFACTURE_INDICATOR, cabecera[1]);
                datosLecturaMap.put(SERIAL_WASHER, cabecera[2]);
                datosLecturaMap.put(BRAND_WASHER, cabecera[3]);
                datosLecturaMap.put(USER_NAME, cabecera[7]);

            }
            case STERILIZER_RESULTS -> {
                /*
                  SDD-1.1 Manufacture lndicator
                  SDD-2.1 Serial Sterilizer
                  SDD-3 Brand Sterilizer
                  SDD-7 UserName, UserlastName
                 */

                datosLecturaMap.put(MANUFACTURE_INDICATOR, cabecera[1]);
                datosLecturaMap.put(SERIAL_STERILIZER, cabecera[2]);
                datosLecturaMap.put(BRAND_STERILIZER, cabecera[3]);
                datosLecturaMap.put(USER_NAME, cabecera[7]);

            }
        }
        return datosLecturaMap;
    }

    /**
     * Método que prepara un Map con los datos de la lectura
     *
     * @param lecturaDatos
     * @param datosLecturaMap
     * @param processEnum
     */
    private void procesarDatosLectura(String[] lecturaDatos, Map<String, String> datosLecturaMap, ProcesosEnum processEnum) throws ParseException {
        switch (processEnum) {
            case BI_STERILIZATION_RESULTS -> bodyBiSterilizationResults(lecturaDatos, datosLecturaMap);

            case BI_DISINFECTION_RESULTS -> bodyBiDisinfectionResults(lecturaDatos, datosLecturaMap);

            case PROTEIN_INDICATORS_RESULTS -> bodyProteinIndicatorsResults(lecturaDatos, datosLecturaMap);

            case WASHER_RESULTS -> bodyWasherResults(lecturaDatos, datosLecturaMap);

            case STERILIZER_RESULTS -> bodySterilizerResults(lecturaDatos, datosLecturaMap);

        }
    }

    private static void bodySterilizerResults(String[] lecturaDatos, Map<String, String> datosLecturaMap) throws ParseException {
        datosLecturaMap.put(MANUFACTURE_DATE, DateFormatter.format(lecturaDatos[1]));
        datosLecturaMap.put(EXPOSURE_TIME, (!Strings.isBlank(lecturaDatos[7]) ? String.valueOf(formatearStringAFloat(lecturaDatos[7])) : ""));

        List<String> datosInternos = splitByCaret(lecturaDatos[10], '^', 9);
        datosLecturaMap.put(RESULT, datosInternos.get(1));
        datosLecturaMap.put(CONCENTRATION, datosInternos.get(4));
        datosLecturaMap.put(RELATIVE_DAMPNESS, (!Strings.isBlank(datosInternos.get(6))) ? String.valueOf(formatearStringAFloat(datosInternos.get(6))) : "");
        datosLecturaMap.put(PACKAGE_NUMBER, datosInternos.get(7));
        datosLecturaMap.put(PROGRAM, datosInternos.get(8));

        datosLecturaMap.put(CREATION_TEST, DateFormatter.format(lecturaDatos[11]));
        datosLecturaMap.put(TEMPERATURE, lecturaDatos[15]);
        datosLecturaMap.put(EXPIRATION_DATE, DateFormatter.format(lecturaDatos[16]));

        List<String> datosInternos2 = splitByCaret(lecturaDatos[25], '^', 2);
        List<String> datosSubInternos = splitByCaret(datosInternos2.get(1), '&', 3);

        datosLecturaMap.put(SERIAL_NUMBER_SCANNER, datosInternos2.get(0));
        datosLecturaMap.put(NAME_PRODUCT, datosSubInternos.get(0));
        datosLecturaMap.put(LOTE_PRODUCT, datosSubInternos.get(1));
        datosLecturaMap.put(BRAND, datosSubInternos.get(2));

        List<String> datosInternos3 = splitByCaret(lecturaDatos[28], '^', 2);
        if (datosInternos.size() < 2) {
            datosLecturaMap.put(CYCLE, "");
        } else {
            datosLecturaMap.put(CYCLE, datosInternos3.get(1));
        }
    }

    private static void bodyWasherResults(String[] lecturaDatos, Map<String, String> datosLecturaMap) throws ParseException {
        datosLecturaMap.put(MANUFACTURE_DATE, DateFormatter.format(lecturaDatos[1]));
        datosLecturaMap.put(WASHING_TIME, lecturaDatos[7]);

        List<String> datosInternos = splitByCaret(lecturaDatos[10], '^', 9);

        datosLecturaMap.put(RESULT, datosInternos.get(1));
        datosLecturaMap.put(DETERGENT_TYPE, datosInternos.get(4));
        datosLecturaMap.put(DETERGENT_CONCENTRATION, datosInternos.get(5));
        datosLecturaMap.put(PROGRAM, datosInternos.get(6));
        datosLecturaMap.put(LOCATION, datosInternos.get(7));
        datosLecturaMap.put(CREATION_TEST, DateFormatter.format(lecturaDatos[11]));
        datosLecturaMap.put(TEMPERATURE, lecturaDatos[15]);
        datosLecturaMap.put(EXPIRATION_DATE, DateFormatter.format(lecturaDatos[16]));

        List<String> datosInternos2 = splitByCaret(lecturaDatos[25], '^', 2);
        List<String> datosSubInternos = splitByCaret(datosInternos2.get(1), '&', 3);

        datosLecturaMap.put(SERIAL_NUMBER_SCANNER, datosInternos2.get(0));
        datosLecturaMap.put(PRODUCT_NAME, datosSubInternos.get(0));
        datosLecturaMap.put(LOT_PRODUCT, datosSubInternos.get(1));
        datosLecturaMap.put(BRAND, datosSubInternos.get(2));

        List<String> datosInternos3 = splitByCaret(lecturaDatos[28], '^', 2);
        if (datosInternos3.size() < 2) {
            datosLecturaMap.put(CYCLE, "");
        } else {
            datosLecturaMap.put(CYCLE, datosInternos3.get(1));
        }

        List<String> datosInternos4 = splitByCaret(lecturaDatos[30], '^', 2);
        List<String> datosSubInternos2 = splitByCaret(datosInternos4.get(1), '&', 2);
        datosLecturaMap.put(WATER_PRESSURE, (!Strings.isBlank(datosSubInternos2.get(0)) ? String.valueOf(formatearStringAFloat(datosSubInternos2.get(0))) : ""));
        datosLecturaMap.put(WATER_HARDNESS, (!Strings.isBlank(datosSubInternos2.get(1)) ? String.valueOf(formatearStringAFloat(datosSubInternos2.get(1))) : ""));
    }

    private static void bodyProteinIndicatorsResults(String[] lecturaDatos, Map<String, String> datosLecturaMap) throws ParseException {
        datosLecturaMap.put(TICKET_NUMBER, lecturaDatos[5]);
        List<String> datosInternos = splitByCaret(lecturaDatos[10], '^', 9);
        datosLecturaMap.put(RESULT, datosInternos.get(1));
        datosLecturaMap.put(SURFACE, datosInternos.get(7));
        datosLecturaMap.put(PROGRAM_NUMBER, datosInternos.get(8));

        datosLecturaMap.put(STARTED_TIME, DateFormatter.format(lecturaDatos[11]));
        datosLecturaMap.put(AVERAGE_TEMPERATURE, String.valueOf(formatearStringAFloat(lecturaDatos[15])));
        datosLecturaMap.put(RESULT_DATE, DateFormatter.formatTime(DateFormatter.parseToLocalTime(lecturaDatos[16]).toString()));

        List<String> datosInternos2 = splitByCaret(lecturaDatos[25], '^', 3);
        List<String> datosSubInternos = splitByCaret(datosInternos2.get(1), '&', 3);

        datosLecturaMap.put(INCUBATOR_SERIAL_NUMBER, datosInternos2.get(0));
        datosLecturaMap.put(PRODUCT_NAME, datosSubInternos.get(0));
        datosLecturaMap.put(LOTE_PRODUCT, datosSubInternos.get(1));
        datosLecturaMap.put(PRODUCT_BRAND, datosSubInternos.get(2));
        datosLecturaMap.put(INCUBATOR_NAME, datosInternos2.get(2));

        List<String> datosInternos3 = splitByCaret(lecturaDatos[28], '^', 2);
        if (datosInternos.size() < 2) {
            datosLecturaMap.put(CYCLE, "");
        } else {
            datosLecturaMap.put(CYCLE, datosInternos3.get(1));
        }
    }

    private static void bodyBiDisinfectionResults(String[] lecturaDatos, Map<String, String> datosLecturaMap) throws ParseException {
        datosLecturaMap.put(TICKET_NUMBER, lecturaDatos[5]);
        List<String> datosInternos = splitByCaret(lecturaDatos[10], '^', 9);
        datosLecturaMap.put(RESULT, datosInternos.get(1));
        datosLecturaMap.put(PROCESS, datosInternos.get(4));
        datosLecturaMap.put(CONDITION_SCIB, datosInternos.get(6));
        datosLecturaMap.put(ROOM_ID, datosInternos.get(7));
        datosLecturaMap.put(ROOM_VOLUME, datosInternos.get(8));

        datosLecturaMap.put(STARTED_TIME, DateFormatter.format(lecturaDatos[11]));
        datosLecturaMap.put(AVERAGE_TEMPERATURE, String.valueOf(formatearStringAFloat(lecturaDatos[15])));
        datosLecturaMap.put(RESULT_DATE, DateFormatter.formatTime(DateFormatter.parseToLocalTime(lecturaDatos[16]).toString()));

        List<String> datosInternos2 = splitByCaret(lecturaDatos[25], '^', 3);
        List<String> datosSubInternos = splitByCaret(datosInternos2.get(1), '&', 3);

        datosLecturaMap.put(INCUBATOR_SERIAL_NUMBER, datosInternos2.get(0));
        datosLecturaMap.put(PRODUCT_NAME, datosSubInternos.get(0));
        datosLecturaMap.put(PRODUCT_LOT, datosSubInternos.get(1));
        datosLecturaMap.put(PRODUCT_BRAND, datosSubInternos.get(2));
        datosLecturaMap.put(INCUBATOR_NAME, datosInternos2.get(2));
    }

    private static void bodyBiSterilizationResults(String[] lecturaDatos, Map<String, String> datosLecturaMap) throws ParseException {
        datosLecturaMap.put(TICKET_NUMBER, lecturaDatos[5]);
        List<String> datosInternos = splitByCaret(lecturaDatos[10], '^', 9);
        datosLecturaMap.put(RESULT, datosInternos.get(1));
        datosLecturaMap.put(PROCESS, datosInternos.get(4));
        datosLecturaMap.put(CONDITION_SCIB, datosInternos.get(6));
        datosLecturaMap.put(LOAD_NUMBER, datosInternos.get(7));
        datosLecturaMap.put(PROGRAM_NUMBER, datosInternos.get(8));

        // format String to dateTime
        datosLecturaMap.put(STARTED_TIME, DateFormatter.format(lecturaDatos[11]));

        datosLecturaMap.put(AVERAGE_TEMPERATURE, String.valueOf(formatearStringAFloat(lecturaDatos[15])));

        datosLecturaMap.put(RESULT_DATE, DateFormatter.formatTime(DateFormatter.parseToLocalTime(lecturaDatos[16]).toString()));

        List<String> datosInternos2 = splitByCaret(lecturaDatos[25], '^', 3);
        List<String> datosSubInternos = splitByCaret(datosInternos2.get(1), '&', 3);

        datosLecturaMap.put(INCUBATOR_SERIAL_NUMBER, datosInternos2.get(0));
        datosLecturaMap.put(PRODUCT_NAME, datosSubInternos.get(0));
        datosLecturaMap.put(PRODUCT_LOT, datosSubInternos.get(1));
        datosLecturaMap.put(PRODUCT_BRAND, datosSubInternos.get(2));
        datosLecturaMap.put(INCUBATOR_NAME, datosInternos2.get(2));

        List<String> datosInternos3 = splitByCaret(lecturaDatos[28], '^', 2);
        if (datosInternos.size() < 2) {
            datosLecturaMap.put(CYCLE, "");
        } else {
            datosLecturaMap.put(CYCLE, datosInternos3.get(1));
        }
    }

    private Map<String, List<String>> resultadoDatosDispositivoYLectura(String cadena) {
        Map<String, List<String>> resultado = new HashMap<>();
        // Filtrar la lista original para obtener los elementos que contengan la palabra "SDD"
        List<String> resultadoDatosDispositivo = Arrays.stream(splitString(cadena))
                .filter(s -> s.contains("SDD"))
                .toList();

        // Filtrar la lista original para obtener los elementos que contengan la palabra "SCD"
        List<String> resultadoDatosLectura = Arrays.stream(splitString(cadena))
                .filter(s -> s.contains("SCD"))
                .toList();

        resultado.put(RESULTADO_DATOS_DISP, resultadoDatosDispositivo);
        resultado.put(RESULTADO_DATOS_LECTURA, resultadoDatosLectura);
        return resultado;
    }

    private static String[] splitString(String cadena) {
        return cadena.replace("\"", "").split("\\\\r");
    }


    /**
     * Método que permite dividir una cadena de caracteres en partes, según un delimitador y la cantidad de partes
     *
     * @param input
     * @return
     */
    public static List<String> splitByCaret(String input, char separator, int size) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (currentChar == separator) {
                if (currentPart.isEmpty()) {
                    parts.add("");
                } else {
                    parts.add(currentPart.toString());
                    currentPart.setLength(0);
                }
            } else {
                currentPart.append(currentChar);
            }
        }

        if (!currentPart.isEmpty()) {
            parts.add(currentPart.toString());
        }

        while (parts.size() < size) {
            parts.add("");
        }

        if (parts.size() > size) {
            parts = parts.subList(0, size);
        }

        return parts;
    }


    /**
     * Realiza una petición GET con un body
     *
     * @param token
     * @param genericDTO
     * @param method
     * @return
     */
    private String genericGetWithBodyResult(String token, @RequestBody GenericDTO genericDTO, String method) {
        try {
            JSONObject body = new JSONObject();
            body.put("From", genericDTO.getFrom());
            body.put("To", genericDTO.getTo());
            body.put("StatusRead", genericDTO.getStatusRead());

            StringEntity stringEntity = new StringEntity(body.toString());

            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

            HttpGetWithBody httpGetWithEntity = new HttpGetWithBody(basePath + method);
            httpGetWithEntity.setEntity(stringEntity);
            httpGetWithEntity.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
            httpGetWithEntity.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
            httpGetWithEntity.setHeader("Authorization", "Bearer " + token);

            try (CloseableHttpResponse response = closeableHttpClient.execute(httpGetWithEntity)) {
                HttpEntity httpGetEntity = response.getEntity();
                if (httpGetEntity != null) {
                    return EntityUtils.toString(httpGetEntity);
                }
            }
        } catch (JSONException | IOException e) {
            throw new GenericGetRequestException("Error: " + e.getMessage());
        }
        return null;

    }

    // Método que permite convertir un String a un Float
    public static float formatearStringAFloat(String numeroString) throws ParseException {
        // Utilizar NumberFormat para manejar diferentes formatos locales de números
        NumberFormat nf = NumberFormat.getInstance();
        Number numero = nf.parse(numeroString);
        return numero.floatValue();
    }

    private static String convert(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // Manejar excepciones según corresponda
            logger.error("Error al convertir el objeto a JSON");
            throw new BaxenGenericException("Error al convertir el objeto a JSON");
        }

        return jsonString;
    }
}

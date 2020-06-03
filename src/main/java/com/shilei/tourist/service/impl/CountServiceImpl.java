package com.shilei.tourist.service.impl;

import com.shilei.tourist.dao.*;
import com.shilei.tourist.entity.*;
import com.shilei.tourist.service.CountService;
import com.shilei.tourist.utils.Camera;
import com.shilei.tourist.utils.HolidayUtil;
import com.shilei.tourist.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.shilei.tourist.utils.Sample.body_num;

@Component
@Slf4j
public class CountServiceImpl implements CountService {
    @Autowired
    NumberDao numberDao;

    @Autowired
    AddressDao addressDao;

    @Autowired
    EveryDayAvgDao everyDayAvgDao;

    @Autowired
    EveryDayMaxDao everyDayMaxDao;

    @Autowired
    StaffDao staffDao;

    @Autowired
    WarningInfoDao warningInfoDao;


    @Override
    public PersonCountVO personCount() throws InterruptedException {
        Camera camera = new Camera();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        camera.main();
        log.info("通过摄像头获取当前的人数为{}",body_num());
        Integer personNumbers = body_num();
        PersonCountVO personCountVO = new PersonCountVO();
        personCountVO.setNowTime(simpleDateFormat.format(new Date()));
        personCountVO.setPersonNumber(personNumbers);
        personCountVO.setPlaceName("测试");
        return personCountVO;
    }

    @Override
    public void savePersonNumber() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        Address address = addressDao.findAddressByName("1号地点");
        Staff staff = staffDao.findStaffByAddressAndState("1号地点","在职");
        Integer personNumbers = body_num();
        PersonNumber personNumber = new PersonNumber();
        personNumber.setAddress("1号地点");
        personNumber.setNumber(personNumbers);
        if (personNumbers > address.getWarningNumber()){
            WarningInfo warningInfo = new WarningInfo();
            warningInfo.setNumber(personNumbers);
            warningInfo.setAddress("1号地点");
            warningInfo.setDate(simple.format(date));
            warningInfo.setDatetime(simpleDateFormat.format(date));
            warningInfo.setWarningNumber(address.getWarningNumber());
            warningInfo.setEmail(staff.getMail());
            warningInfo.setPhone(staff.getPhone());
            warningInfo.setName(staff.getName());
            warningInfoDao.save(warningInfo);
        }
        personNumber.setNowTime(new Date());
        personNumber.setDate(simple.format(date));
        personNumber.setHour(hour.format(date));
        log.info("person_number表新增数据为{}",personNumber);
        numberDao.save(personNumber);
        numberDao.flush();
    }

    @Override
    public void saveAvgToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        List<Address> addressList = addressDao.findAll();
        List<String> addressNameList = addressList.stream().map(e -> e.getName()).collect(Collectors.toList());
        for (String address: addressNameList) {
            EveryDayAvg todayAvg = numberDao.findAllByDateAndAddress(simpleDateFormat.format(date),address);
            everyDayAvgDao.save(todayAvg);
        }
    }

    @Override
    public List<TomorrowNumberVO> tomorrowNumber() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = simpleDateFormat.format(date);
        List<TomorrowNumberVO> tomorrowNumberVOList = new ArrayList<>();
        List<Address> addressList = addressDao.findAll();
        List<String> addressNameList = addressList.stream().map(e -> e.getName()).collect(Collectors.toList());
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();
        String holiday = HolidayUtil.Get(day.format(date));
        for (String address:addressNameList) {
            TomorrowNumberVO tomorrowNumberVO = new TomorrowNumberVO();
            List<PersonNumber> personNumberList = numberDao.findAllByAddressAndDate(address,today);
            log.info("查找到的personNumberList为{}",personNumberList);
            if (!personNumberList.isEmpty()){
                PersonNumber personNumber = personNumberList.stream().max(Comparator.comparingInt(e ->e.getNumber())).get();

                tomorrowNumberVO.setAddress(personNumber.getAddress());
                tomorrowNumberVO.setDate(personNumber.getDate());
                tomorrowNumberVO.setTodayMost(personNumber.getNumber());

                int todayTotal = personNumberList.stream().mapToInt(e -> e.getNumber()).sum() / personNumberList.size();
                tomorrowNumberVO.setTodayTotal(todayTotal);
                List<PersonNumber> numberList = numberDao.findAllByAddressAndDateOrderByNowTimeDesc(address,today);
                tomorrowNumberVO.setNowNumber(numberList.get(0).getNumber());
                tomorrowNumberVO.setTag(holiday);

                List<Integer> list1 = everyDayAvgDao.findAllAvgNumberByAddress(address);
                List<Double> doubleList1 = new ArrayList<>();
                for (Integer a:list1) {
                    double b = a;
                    doubleList1.add(b);
                }
                List<Integer> list2= everyDayMaxDao.findAllMaxNumberByAddress(address);
                List<Double> doubleList2 = new ArrayList<>();
                for (Integer a:list2) {
                    double b = a;
                    doubleList2.add(b);
                }
                Double tomorrowTotal = getExpect(doubleList1,1,0.6);
                Double tomorrowMax = getExpect(doubleList2,1,0.6);
                if(holiday.equals("节日")){
                    tomorrowNumberVO.setTomorrowMost(new Double(tomorrowMax*3).intValue());
                    tomorrowNumberVO.setTomorrowTotal(new Double(tomorrowTotal*3).intValue());
                }else if(holiday.equals("假日")){
                    tomorrowNumberVO.setTomorrowMost(new Double(tomorrowMax*2).intValue());
                    tomorrowNumberVO.setTomorrowTotal(new Double(tomorrowTotal*2).intValue());
                }else {
                    tomorrowNumberVO.setTomorrowMost(new Double(tomorrowMax).intValue());
                    tomorrowNumberVO.setTomorrowTotal(new Double(todayTotal).intValue());
                }
                tomorrowNumberVOList.add(tomorrowNumberVO);
            }
        }


        return tomorrowNumberVOList;
    }

    private static Double getExpect(List<Double> list, int year, Double modulus ) {
        if (list.size() < 10 || modulus <= 0 || modulus >= 1) {
            return null;
        }
        Double modulusLeft = 1 - modulus;
        Double lastIndex = list.get(0);
        Double lastSecIndex = list.get(0);
        for (Double data :list) {
            lastIndex = modulus * data + modulusLeft * lastIndex;
            lastSecIndex = modulus * lastIndex + modulusLeft * lastSecIndex;
        }
        Double a = 2 * lastIndex - lastSecIndex;
        Double b = (modulus / modulusLeft) * (lastIndex - lastSecIndex);
        return a + b * year;
    }

    @Override
    public List<TodayCountVO> todayPersonNumber(TodayCountDTO todayCountDTO) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = null;
        if (null == todayCountDTO.getDate() || todayCountDTO.getDate().equals(null) || todayCountDTO.getDate().isEmpty()){
            Date today = new Date();
            todayCountDTO.setDate(simpleDateFormat.format(today));
        }else{
            String TimeStart = todayCountDTO.getDate().replace("Z", " UTC");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            Date callbackTimeStart = format.parse(TimeStart);    //Fri Dec 28 00:00:00 GMT+08:00 2018
            date = simpleDateFormat.format(callbackTimeStart);
            todayCountDTO.setDate(date);
        }

        List<TodayCountVO> series = new ArrayList<>();

        TodayCountVO todayCountVO_Max = new TodayCountVO();
        List<String> avgList = numberDao.findAvgByDateAndAddress(todayCountDTO.getDate(),todayCountDTO.getAddress());
        todayCountVO_Max.setName("小时平均");
        todayCountVO_Max.setType("line");
        todayCountVO_Max.setData(avgList);
        series.add(todayCountVO_Max);

        TodayCountVO todayCountVO_Avg = new TodayCountVO();
        List<String> maxList = numberDao.findMaxByDateAndAddress(todayCountDTO.getDate(),todayCountDTO.getAddress());
        todayCountVO_Avg.setName("小时最高");
        todayCountVO_Avg.setType("line");
        todayCountVO_Avg.setData(maxList);
        series.add(todayCountVO_Avg);

        log.info("当日分时统计数据为{}",series);
        return series;
    }

    @Override
    public List<MoreDayCountVO> moreDayCount(MoreDayCountDTO moreDayCountDTO) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (null == moreDayCountDTO.getEndDate()  || null == moreDayCountDTO.getEndDate()){
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7);
            Date sevenDayAgo = calendar.getTime();
            moreDayCountDTO.setStartDate(simpleDateFormat.format(sevenDayAgo));
            moreDayCountDTO.setEndDate(simpleDateFormat.format(date));
        }else{
            String TimeStart = moreDayCountDTO.getStartDate().replace("Z", " UTC");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            Date callbackTimeStart = format.parse(TimeStart);    //Fri Dec 28 00:00:00 GMT+08:00 2018
            String startDate  = simpleDateFormat.format(callbackTimeStart);
            moreDayCountDTO.setStartDate(startDate);
            String TimeEnd = moreDayCountDTO.getEndDate().replace("Z", " UTC");
            Date callbackTimeEnd = format.parse(TimeEnd);    //Fri Dec 28 00:00:00 GMT+08:00 2018
            String endDate  = simpleDateFormat.format(callbackTimeEnd);
            moreDayCountDTO.setEndDate(endDate);
        }


        List<MoreDayCountVO> series = new ArrayList<>();

        MoreDayCountVO moreDayCountVO_Max = new MoreDayCountVO();
        List<String> moreDayMax = everyDayMaxDao.findEveryDayMaxByDateAndAddress(moreDayCountDTO.getStartDate(),moreDayCountDTO.getEndDate(),moreDayCountDTO.getAddress());
        moreDayCountVO_Max.setName("当日最高");
        moreDayCountVO_Max.setType("line");
        moreDayCountVO_Max.setData(moreDayMax);
        series.add(moreDayCountVO_Max);

        MoreDayCountVO moreDayCountVO_Avg = new MoreDayCountVO();
        List<String> moreDayAvg = everyDayAvgDao.findEveryDayAvgByDateAndAddress(moreDayCountDTO.getStartDate(),moreDayCountDTO.getEndDate(),moreDayCountDTO.getAddress());
        moreDayCountVO_Avg.setName("当日平均");
        moreDayCountVO_Avg.setType("line");
        moreDayCountVO_Avg.setData(moreDayAvg);
        series.add(moreDayCountVO_Avg);
        log.info("查询到的多日数据为{}",series);
        return series;
    }

    @Override
    public List<String> getDate(MoreDayCountDTO moreDayCountDTO) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (null == moreDayCountDTO.getEndDate() || null == moreDayCountDTO.getEndDate()) {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7);
            Date sevenDayAgo = calendar.getTime();
            moreDayCountDTO.setStartDate(simpleDateFormat.format(sevenDayAgo));
            moreDayCountDTO.setEndDate(simpleDateFormat.format(date));
        } else {
            String TimeStart = moreDayCountDTO.getStartDate().replace("Z", " UTC");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            Date callbackTimeStart = format.parse(TimeStart);    //Fri Dec 28 00:00:00 GMT+08:00 2018
            String startDate = simpleDateFormat.format(callbackTimeStart);
            moreDayCountDTO.setStartDate(startDate);
            String TimeEnd = moreDayCountDTO.getEndDate().replace("Z", " UTC");
            Date callbackTimeEnd = format.parse(TimeEnd);    //Fri Dec 28 00:00:00 GMT+08:00 2018
            String endDate = simpleDateFormat.format(callbackTimeEnd);
            moreDayCountDTO.setEndDate(endDate);
        }

        List<String> list = new ArrayList<>();
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(simpleDateFormat.parse(moreDayCountDTO.getStartDate()));
        endDay.setTime(simpleDateFormat.parse(moreDayCountDTO.getEndDate()));
        // 给出的日期开始日比终了日大则不执行打印
        String s1 = moreDayCountDTO.getStartDate();
        list.add(s1);
        if (!moreDayCountDTO.getStartDate().equals(moreDayCountDTO.getEndDate())) {
            if (startDay.compareTo(endDay) <= 0) {
                //现在打印中的日期
                Calendar currentPrintDay = startDay;
                while (true) {
                    // 日期加一
                    currentPrintDay.add(Calendar.DATE, 1);
                    // 日期加一后判断是否达到终了日，达到则终止打印
                    if (currentPrintDay.compareTo(endDay) == 0) {
                        break;
                    }
                    String s = simpleDateFormat.format(currentPrintDay.getTime());
                    list.add(s);
                }
                String s2 = moreDayCountDTO.getEndDate();
                list.add(s2);
            }
        }
        return list;
    }

    @Override
    public List<NumberWarningVO> getNumberWarning() throws Exception {
        List<NumberWarningVO> list =  castEntity(staffDao.getWarningNumber(),NumberWarningVO.class);
        log.info("多表查询返回的预警设置表为{}",list);
        return list;
    }

    @Override
    public List<Map<String,String>> getYear() {
        List<Map<String,String>> list = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        Map<String,String> map = new HashMap<>();
        map.put("value","选项1");
        map.put("label", String.valueOf(year));
        list.add(map);
        Map<String,String> map2 = new HashMap<>();
        map2.put("value","选项2");
        map2.put("label", String.valueOf(year-1));
        list.add(map2);
        Map<String,String> map3 = new HashMap<>();
        map3.put("value","选项3");
        map3.put("label", String.valueOf(year-2));
        list.add(map3);
        return list;
    }

    @Override
    public List<Map<String,String>> getMonth(int year) {
        List<Map<String,String>> list = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        if(year == date.get(Calendar.YEAR)){
            int month = date.get(Calendar.MONTH)+1;
            for (int i = 1; i <= month; i++) {
                Map<String,String> map = new HashMap<>();
                map.put("value","选项"+i);
                map.put("label", String.valueOf(i));
                list.add(map);
            }
        }else{
            for (int i = 1; i <= 12; i++) {
                Map<String,String> map = new HashMap<>();
                map.put("value","选项"+i);
                map.put("label", String.valueOf(i));
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public List<MoreDayCountVO> monthCount(MonthCountDTO monthCountDTO) {
        MoreDayCountDTO moreDayCountDTO = new MoreDayCountDTO();
        moreDayCountDTO.setStartDate(getFirstDayOfMonth(monthCountDTO.getYear(),monthCountDTO.getMonth()));
        moreDayCountDTO.setEndDate(getLastDayOfMonth(monthCountDTO.getYear(),monthCountDTO.getMonth()));
        moreDayCountDTO.setAddress(monthCountDTO.getAddress());

        List<MoreDayCountVO> series = new ArrayList<>();

        MoreDayCountVO moreDayCountVO_Max = new MoreDayCountVO();
        List<String> moreDayMax = everyDayMaxDao.findEveryDayMaxByDateAndAddress(moreDayCountDTO.getStartDate(),moreDayCountDTO.getEndDate(),moreDayCountDTO.getAddress());
        moreDayCountVO_Max.setName("当日最高");
        moreDayCountVO_Max.setType("line");
        moreDayCountVO_Max.setData(moreDayMax);
        series.add(moreDayCountVO_Max);

        MoreDayCountVO moreDayCountVO_Avg = new MoreDayCountVO();
        List<String> moreDayAvg = everyDayAvgDao.findEveryDayAvgByDateAndAddress(moreDayCountDTO.getStartDate(),moreDayCountDTO.getEndDate(),moreDayCountDTO.getAddress());
        moreDayCountVO_Avg.setName("当日平均");
        moreDayCountVO_Avg.setType("line");
        moreDayCountVO_Avg.setData(moreDayAvg);
        series.add(moreDayCountVO_Avg);
        log.info("查询到的多日数据为{}",series);
        return series;

    }

    @Override
    public List<MoreDayCountVO> weekCount(TodayCountDTO todayCountDTO) throws ParseException {
        String startDate = getAllDate(todayCountDTO).get(0);
        String endDate = getAllDate(todayCountDTO).get(getAllDate(todayCountDTO).size() - 1);

        List<MoreDayCountVO> series = new ArrayList<>();

        MoreDayCountVO moreDayCountVO_Max = new MoreDayCountVO();
        List<String> moreDayMax = everyDayMaxDao.findEveryDayMaxByDateAndAddress(startDate,endDate,todayCountDTO.getAddress());
        moreDayCountVO_Max.setName("当日最高");
        moreDayCountVO_Max.setType("line");
        moreDayCountVO_Max.setData(moreDayMax);
        series.add(moreDayCountVO_Max);

        MoreDayCountVO moreDayCountVO_Avg = new MoreDayCountVO();
        List<String> moreDayAvg = everyDayAvgDao.findEveryDayAvgByDateAndAddress(startDate,endDate,todayCountDTO.getAddress());
        moreDayCountVO_Avg.setName("当日平均");
        moreDayCountVO_Avg.setType("line");
        moreDayCountVO_Avg.setData(moreDayAvg);
        series.add(moreDayCountVO_Avg);
        log.info("查询到的多日数据为{}",series);
        return series;

    }

    @Override
    public List<String> getAllDate(TodayCountDTO todayCountDTO) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (todayCountDTO.getDate().isEmpty()){
            Date date = new Date();
            cal.setTime(sdf.parse(sdf.format(date)));
        }else{
            cal.setTime(sdf.parse(todayCountDTO.getDate()));
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 获取该周第一天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day - 1);
        String startDate = sdf.format(cal.getTime());
        // 获取该周最后一天
        cal.add(Calendar.DATE, 7);
        String endDate = sdf.format(cal.getTime());

        MoreDayCountDTO moreDayCountDTO = new MoreDayCountDTO();
        moreDayCountDTO.setStartDate(startDate);
        moreDayCountDTO.setEndDate(endDate);
        moreDayCountDTO.setAddress(todayCountDTO.getAddress());

        List<String> list = new ArrayList<>();
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(sdf.parse(moreDayCountDTO.getStartDate()));
        endDay.setTime(sdf.parse(moreDayCountDTO.getEndDate()));
        if (!moreDayCountDTO.getStartDate().equals(moreDayCountDTO.getEndDate())) {
            if (startDate.compareTo(endDate) <= 0) {
                //现在打印中的日期
                Calendar currentPrintDay = startDay;
                while (true) {
                    // 日期加一
                    currentPrintDay.add(Calendar.DATE, 1);
                    // 日期加一后判断是否达到终了日，达到则终止打印
                    if (currentPrintDay.compareTo(endDay) == 0) {
                        break;
                    }
                    String s = sdf.format(currentPrintDay.getTime());
                    list.add(s);
                }
                String s2 = moreDayCountDTO.getEndDate();
                list.add(s2);
            }
        }
        return list;
    }


    public static List<String> getDayList(MoreDayCountDTO moreDayCountDTO) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = new ArrayList<>();
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(simpleDateFormat.parse(moreDayCountDTO.getStartDate()));
        endDay.setTime(simpleDateFormat.parse(moreDayCountDTO.getEndDate()));
        if (!moreDayCountDTO.getStartDate().equals(moreDayCountDTO.getEndDate())) {
            if (moreDayCountDTO.getStartDate().compareTo(moreDayCountDTO.getEndDate()) <= 0) {
                //现在打印中的日期
                Calendar currentPrintDay = startDay;
                list.add(moreDayCountDTO.getStartDate());
                while (true) {
                    // 日期加一
                    currentPrintDay.add(Calendar.DATE, 1);
                    // 日期加一后判断是否达到终了日，达到则终止打印
                    if (currentPrintDay.compareTo(endDay) == 0) {
                        break;
                    }
                    String s = simpleDateFormat.format(currentPrintDay.getTime());
                    list.add(s);
                }
                String s2 = moreDayCountDTO.getEndDate();
                list.add(s2);
            }
        }
        return list;

    }


    @Override
    public List<HomeInfoGetVO> getHomeInfo(String address) {
        List<HomeInfoGetVO> list = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String avg = everyDayAvgDao.findAvgNumberByDateAndAddress(address,simpleDateFormat.format(date));
        String max = everyDayMaxDao.findMaxNumberByDateAndAddress(address,simpleDateFormat.format(date));
        String min = numberDao.findMinNumberByDateAndAddress(address,simpleDateFormat.format(date));
        String now = numberDao.findNumberByAddressAndDate(address,simpleDateFormat.format(date));
        String name = staffDao.findStaffByAddress(address);
        HomeInfoGetVO homeInfoGetVO = new HomeInfoGetVO();
        homeInfoGetVO.setAvg(avg == null ? 50 : Integer.parseInt(avg));
        homeInfoGetVO.setMax(max == null ? 0 : Integer.parseInt(max));
        homeInfoGetVO.setMin(min == null ? 0 : Integer.parseInt(min));
        homeInfoGetVO.setNow(now == null ? 0 : Integer.parseInt(now));
        homeInfoGetVO.setStaff(name);
        log.info("首页查询到的homeInfoVO为{}",homeInfoGetVO);
        list.add(homeInfoGetVO);
        return list;
    }

    //转换实体类
    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) throws Exception {
        List<T> returnList = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return returnList;
        }
        List<Class<?>> allFieldsList = getAllFieldsList(NumberWarningVO.class);
        Class[] mod = allFieldsList.toArray(new Class[list.get(0).length]);
        for (Object[] co : list) {
            Constructor<T> constructor = clazz.getConstructor(mod);
            returnList.add(constructor.newInstance(co));
        }
        return returnList;
    }
    //通过反射获取实体类的每个字段类型
    public static List<Class<?>> getAllFieldsList(final Class<?> cls) {
        Validate.isTrue(cls != null, "The class must not be null");
        final List<Class<?>> allFields = new ArrayList<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                allFields.add(field.getType());
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }


    /**
     * 获取指定年月的第一天
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取指定年月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

}

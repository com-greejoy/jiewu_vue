package com.ruoyi.common.utils.file;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.mapper.JwTeamMapper;
import com.ruoyi.project.jiewu.service.JwGameItemService;
import com.ruoyi.project.jiewu.service.JwMatchService;
import com.ruoyi.project.jiewu.service.JwSignRecordService;
import com.ruoyi.project.jiewu.service.JwSportService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PdfUtils {

	@Autowired
	private JwMatchService jwMatchService;

	@Autowired
	private JwSportService jwSportService;

	@Autowired
	private JwTeamMapper jwTeamMapper;

	@Autowired
	private JwSignRecordService jwSignRecordService;

	@Autowired
	private JwGameItemService jwGameItemService;

	private static String GAMENAME = "\n“全域天府 舞遍四川”体育舞蹈比赛暨\n四川省第三十一届体育舞蹈（国标舞）锦标赛";
	private static String tempFile = "E:/jz/项目/jieWu/2024乐山小学生/赛程/模板.pdf";

	public static PdfReader getStampedReader(Map<String, String> map) throws Exception {
        // 读取pdf模板
		PdfReader reader = new PdfReader(tempFile);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PdfStamper ps = new PdfStamper(reader, bos);
		/*定义字体*/
		BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
		BaseFont numFont = BaseFont.createFont("C:/windows/fonts/ARLRDBD.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		ArrayList<BaseFont> fonts = new ArrayList<BaseFont>();
		fonts.add(baseFont);
		fonts.add(numFont);
		AcroFields fields = ps.getAcroFields();
		fields.setSubstitutionFonts(fonts);
		//单独设置字体

		fields.setFieldProperty("backNumber", "textfont", numFont, null);
//		fields.setFieldProperty("backNumber", "textcolor", BaseColor.WHITE, null);
//		fields.setFieldProperty("department", "textcolor", BaseColor.WHITE, null);
		fields.setFieldProperty("department", "textsize", 40, null);
//		fields.setFieldProperty("name", "textcolor", BaseColor.WHITE, null);
//		fields.setFieldProperty("game1", "textcolor", BaseColor.WHITE, null);
		fields.setFieldProperty("game1", "setfflags", PdfFormField.FF_MULTILINE, null);
		//往pdf模版里面设置值
//		fields.setField("gameName", map.get("gameName"));
//		fields.setField("background", "");//背景
		fields.setField("backNumber", map.get("backNumber"));
		fields.setField("department", map.get("department"));
		fields.setField("name", map.get("name"));
		fields.setField("game1", map.get("game1"));
		fields.setFieldProperty("indexOrder", "textfont", numFont, null);
		fields.setField("indexOrder", map.get("indexOrder"));
//		fields.setField("game18", map.get("game2"));
//		fields.setField("game3", map.get("game3"));
//		fields.setField("game4", map.get("game4"));
//		fields.setField("game5", map.get("game5"));
//		fields.setField("game66", map.get("game6"));
//		fields.setField("game28", map.get("game7"));
//		fields.setField("game38", map.get("game8"));
//		fields.setField("game48", map.get("game9"));
//		fields.setField("game58", map.get("game10"));
//		 如果为false那么生成的PDF文件还能编辑，一定要设为true
		ps.setFormFlattening(true);


		ps.close();
		reader.close();
		return new PdfReader(bos.toByteArray());
	}

	public List<PdfReader> getPdfReaderList(Long matchId) throws Exception {

		List<PdfReader> list = new ArrayList<PdfReader>();

		// 全部队伍
		JwTeam query = new JwTeam();
		query.setMatchId(matchId);
		List<JwTeam> jwTeamList = jwTeamMapper.selectJwTeamListWithMatch(query);

		if(jwTeamList != null){
			Long lastBackNum = 0l;
			for(JwTeam jwTeam : jwTeamList){

				// 获取代表队所有背号
				List<String> jwSignRecords = jwSignRecordService.selectSignBackNumList(matchId, jwTeam.getId());

				if(jwSignRecords != null && jwSignRecords.size() > 0){
					for(String backNumber : jwSignRecords){
						String userNameS = "";
						String gameItemName = "";
						String indexOrder = "";
						// 获取 背号 的全部赛程
						List<JwSignRecord> backList = jwSignRecordService.selectScheduleByBackNum(matchId, backNumber);
						if(backList != null && backList.size() > 0){
							for(JwSignRecord jwSignRecord : backList){
								List<JwSignRecordSport> jwSignRecordSportList = jwSignRecord.getJwSignRecordSportList();
								userNameS = (jwSignRecordSportList.stream().map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(" ")));
								gameItemName += DateUtils.parseDateToStr("HH:mm", jwSignRecord.getPlaceTime()) +"  " + jwSignRecord.getItemName() +"\r\n";
								indexOrder += (jwSignRecord.getItemName().split(":")[0]) + " : " + jwSignRecord.getIndexOrder() +"\r\n";
							}
						}
						Map<String, String> map = new HashMap<String, String>();
						map.put("gameName", GAMENAME);
						map.put("backNumber", backNumber);
						map.put("department", jwTeam.getIndexOrder() +". "+ jwTeam.getTeamName());
						map.put("name", userNameS);
						map.put("game1", gameItemName);
						map.put("indexOrder", String.valueOf(indexOrder));

						list.add(getStampedReader(map));

						if(lastBackNum < Long.valueOf(backNumber)){
							lastBackNum = Long.valueOf(backNumber);
						}
					}
				}
			}

			// 再多增加 20个背号
			for(int i = 1; i<= 5; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("gameName", GAMENAME);
				map.put("backNumber", new DecimalFormat("000").format(lastBackNum + i));
				map.put("department", "");
				map.put("name", "");
				map.put("game1", "");
				map.put("indexOrder", "");


				list.add(getStampedReader(map));
			}
		}
		return list;
	}

	public void generatePdf(OutputStream out, Long matchId) throws Exception {
		List<PdfReader> readers = getPdfReaderList(matchId);
        Document document = new Document();
		PdfCopy copy = new PdfCopy(document, out);
		document.open();
		for (PdfReader reader : readers){
			document.newPage();
			copy.addDocument(reader);
		}
		copy.close();
	}

	public static void showFontName() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontFamilyNames = env.getAvailableFontFamilyNames(); // 当然可以直接获取Font列表
		for(String fontFamilyName : fontFamilyNames) {
			System.out.println(fontFamilyName);
		}
	}

	public static void main(String[] args) throws Exception {
//		long t1 = System.currentTimeMillis();
//		OutputStream out = new FileOutputStream("d:/背番号/11.pdf");
//		generatePdf(out);
//		System.out.println("耗时："+(System.currentTimeMillis()-t1)/1000+"秒");
	}

	// 生成背号
	public void genPdf(Long matchId)  {
		try {
			long t1 = System.currentTimeMillis();
			OutputStream out = new FileOutputStream("E:/jz/项目/jieWu/2024乐山小学生/赛程/全部.pdf");
			generatePdf(out, matchId);
			System.out.println("耗时："+(System.currentTimeMillis()-t1)/1000+"秒");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

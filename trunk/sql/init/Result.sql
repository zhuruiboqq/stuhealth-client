
create table Result(
	CHECKID numeric, --ID
	TJND nvarchar(5), --体检年度
	BH nvarchar(32), --编号
	SCHOOLBH varchar(20), --学校编号
	GRADEBH nvarchar(2), --年级编号
	CLASSBH nvarchar(10), --班级编号
	RXSJ nvarchar(4), --入学时间
	XH nvarchar(20), --学号
	SCHOOL_NAME nvarchar(50), --学校名称
	GRADE_NAME nvarchar(30), --年级名称
	CLASS_NAME nvarchar(30), --班级名称
	XM nvarchar(30), --姓名
	XB varchar(10), --性别
	CSRQ datetime, --出生日期
	MZ nvarchar(20), --民族
	ZZMM nvarchar(20), --政治面貌
	SY nvarchar(8), --生源
	TJRQ datetime, --体检日期
	NL int, --年龄
	MARK nvarchar(100), --备注
	SG decimal(18,4), --身高
	SGDJQ nvarchar(8), --身高等级（全国标准)
	SGDJD nvarchar(8), --身高等级(广东标准)
	SGBJ nvarchar(1), --身高是否正常
	TZ decimal(18,4), --体重
	TZDJQ nvarchar(8), --体重等级（全国标准)
	TZDJD nvarchar(8), --体重等级(广东标准)
	TZBJ nvarchar(1), --体重是否正常
	YYPJ nvarchar(10), --营养评价
	YYPJX nvarchar(10), --营养评价X
	TXPJ nvarchar(10), --
	FYPJ nvarchar(12), --
	XW decimal(18,4), --胸围
	XWDJQ nvarchar(8), --胸围等级（全国标准)
	XWDJD nvarchar(8), --胸围等级(广东标准)
	XWBJ nvarchar(1), --胸围是否正常
	FHL decimal(18,4), --肺活量
	FHLDJQ nvarchar(8), --肺活量等级（全国标准)
	FHLDJD nvarchar(8), --肺活量等级(广东标准)
	FHLBJ nvarchar(1), --肺活量是否正常
	WL decimal(18,4), --握力
	WLDJQ nvarchar(8), --握力等级（全国标准)
	WLDJD nvarchar(8), --握力等级(广东标准)
	WLBJ nvarchar(1), --握力是否正常
	MB decimal(18,4), --脉搏
	MBBJ nvarchar(1), --脉搏是否正常
	SSY decimal(18,4), --收缩压
	SSYBJ nvarchar(1), --收缩压是否正常
	SZY decimal(18,4), --舒张压
	SZYBJ nvarchar(1), --舒张压是否正常
	LSL decimal(18,4), --左眼视力
	RSL decimal(18,4), --右眼视力
	QTYB nvarchar(8), --其它眼病
	LQG nvarchar(8), --左眼屈光不正
	RQG nvarchar(8), --右眼屈光不正
	LSY nvarchar(8), --左眼沙眼
	RSY nvarchar(8), --右眼沙眼
	BS nvarchar(8), --辨色
	TL nvarchar(8), --听力
	XJ nvarchar(8), --嗅觉
	BB nvarchar(8), --鼻病
	QSBNUM int, --
	RQB int, --
	RQS int, --
	RQH int, --
	HQB int, --
	HQS int, --
	HQH int, --
	YZB nvarchar(8), --牙周疾病
	BTT nvarchar(8), --扁桃体
	XK nvarchar(8), --胸廓
	JZ nvarchar(8), --脊柱侧弯
	SZ nvarchar(8), --四肢
	PZ nvarchar(8), --平足
	XZ nvarchar(8), --心脏
	GP nvarchar(8), --肝脾
	FEI nvarchar(8), --肺
	XT nvarchar(8), --胸透
	XHDB int, --
	FHCL nvarchar(8), --肠道蠕虫
	PFB nvarchar(8), --皮肤病
	ZG decimal(18,4), --
	ZGDJQ nvarchar(8), --
	ZGDJD nvarchar(8), --
	ZGBJ nvarchar(1), --
	JK decimal(18,4), --肩宽
	JKDJQ nvarchar(8), --肩宽等级（全国标准)
	JKDJD nvarchar(8), --肩宽等级(广东标准)
	JKBJ nvarchar(1), --肩宽是否正常
	GPK decimal(18,4), --
	GPKDJQ nvarchar(8), --
	GPKDJD nvarchar(8), --
	GPKBJ nvarchar(1), --
	BJL decimal(18,4), --
	GSTJ decimal(18,4), --
	JJX decimal(18,4), --
	BMKY nvarchar(8), --表面抗原
	SJSR nvarchar(8), --神经衰弱
	WSM decimal(18,4), --
	WSMDJQ nvarchar(8), --
	WSMDJD nvarchar(8), --
	WSMBJ nvarchar(1), --
	LDTY decimal(18,4), --
	LDTYDJQ nvarchar(8), --
	LDTYDJD nvarchar(8), --
	LDTYBJ nvarchar(1), --
	LL decimal(18,4), --
	LLDJQ nvarchar(8), --
	LLDJD nvarchar(8), --
	LLBJ nvarchar(1), --
	NNL decimal(18,4), --
	NNLDJQ nvarchar(8), --
	NNLDJD nvarchar(8), --
	NNLBJ nvarchar(1), --
	ZWT decimal(18,4), --
	ZWTDJQ nvarchar(8), --
	ZWTDJD nvarchar(8), --
	ZWTBJ nvarchar(1), --
	T1 nvarchar(8), --自定义检查项目
	T2 nvarchar(8), --自定义检查项目
	T3 nvarchar(8), --自定义检查项目
	T4 nvarchar(8), --自定义检查项目
	T5 nvarchar(8), --自定义检查项目
	T6 nvarchar(8), --自定义检查项目
	T7 nvarchar(8), --自定义检查项目
	T8 nvarchar(8), --自定义检查项目
	T9 nvarchar(8), --自定义检查项目
	T10 nvarchar(8), --自定义检查项目
	SLBH nvarchar(1), --
	MBPJ nvarchar(8), --
	SSYPJ nvarchar(8), --
	SZYPJ nvarchar(8), --
	PXPJ nvarchar(8), --
	SLPJ nvarchar(8), --
	XJH varchar(50), --
	YYPJB nvarchar(10), --
	TJSY int, --
	MB1 int, --
	MB2 int, --
	MB3 int, --
	TJFS int, --
	TJPJ nvarchar(8), --
	YYPJH nvarchar(8), --
	XMPY nvarchar(8), --
	HXP decimal(18,4), --
	HXPPJ nvarchar(10), --
	EB nvarchar(8), --耳病
	YB nvarchar(8), --眼病
	TB nvarchar(8), --头部
	JB nvarchar(8), --颈部
	PF nvarchar(8), --皮肤
	JZX nvarchar(8), --甲状腺
	LBJ nvarchar(8), --淋巴结
	JHJS nvarchar(8), --结核菌素
	GBZAM decimal(18,4), --
	DHSA decimal(18,4), --
	DHSZ decimal(18,4), --
	GG nvarchar(8), --肝功
	BMKT nvarchar(8), --表面抗体
	YGHKT nvarchar(8), --乙肝核心抗体
	YGEKY nvarchar(8), --乙肝e抗原
	YGEKT nvarchar(8), --乙肝e抗体
	BLOOD nvarchar(8), --血型
	YW decimal(18,4), --
	TW decimal(18,4), --
	CXBH nvarchar(20), --
	LJSL decimal(18,4), --
	RJSL decimal(18,4), --
	JMY nvarchar(8), --结膜炎
	WGFB nvarchar(8), --窝沟封闭
	YYB nvarchar(8), --牙龈
	PEI nvarchar(8), --脾
	LSLS decimal(18,4), --
	RSLS decimal(18,4), --
	LJSLS decimal(18,4), --
	RJSLS decimal(18,4), --
	RTL nvarchar(10), --听力右
	WSZQ nvarchar(10), --男性外生殖器
	RS nchar(10), --右眼沙眼
	BXP decimal(18,4), --
	XXB decimal(18,4), --
	SFZH nvarchar(50), --
	DH nvarchar(50), --
	ADDRESS nvarchar(100), --地址
	RXYFJZS nchar(100), --
	YMA nvarchar(20), --
	YMAJY nvarchar(100), --
	YMB nvarchar(20), --
	YMBJY nvarchar(100), --
	YMC nvarchar(20), --
	YMCJY nvarchar(100), --
	YMD nvarchar(20), --
	YMDJY nvarchar(100), --
	YME nvarchar(20), --
	YMEJY nvarchar(100), --
	YMQT nvarchar(100), --
	RXHJZS nvarchar(100), --
	JWBS nvarchar(100), --
	QCQFY int, --
	STUDGUID nvarchar(20), --
	SCHOOLID nvarchar(20), --学校ID
	STUDNUM nvarchar(20), --
	YMF nvarchar(20), --
	YMFJY nvarchar(100) --
)
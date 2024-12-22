# Carpooling Application

## Опис на системот
Carpooling апликацијата е Android апликација за споделување на возења која овозможува поврзување помеѓу возачи и патници. 

## Архитектура на системот

### Компоненти
1. **Активности (Activities)**
   - MainActivity - Почетен екран
   - LoginActivity - Најава на корисници
   - RegisterActivity - Регистрација на нови корисници
   - PassengerActivity - Интерфејс за патници
   - DriverActivity - Интерфејс за возачи
   - LocationPickerActivity - Избор на локација
   - RideHistoryActivity - Историја на возења

2. **Модели (Models)**
   - User - Модел за корисници
   - Driver - Модел за возачи
   - Ride - Модел за возења

3. **Адаптери (Adapters)**
   - DriversAdapter - За листање на возачи
   - ActiveRidesAdapter - За листање на активни возења

4. **База на податоци**
   - SQLite база со DatabaseHelper класа

### Технички карактеристики
- Минимална Android верзија: API 21 (Android 5.0)
- Целна Android верзија: API 33 (Android 13)
- Користи Material Design компоненти
- Поддржува портретна и пејзажна ориентација

## Инсталација

1. Клонирајте го репозиториумот: 
2. Отворете го проектот во Android Studio

3. Конфигурирајте го Google Maps API:
   - Добијте API клуч од Google Cloud Console
   - Додадете го клучот во AndroidManifest.xml

4. Изградете го проектот и инсталирајте на уред/емулатор

## Користење на апликацијата

### За патници:
1. Регистрирајте се или најавете се
2. Внесете ја почетната и крајната локација
3. Пребарајте достапни возачи
4. Изберете возач и испратете барање
5. Следете го статусот на возењето

### За возачи:
1. Регистрирајте се или најавете се како возач
2. Поставете ја вашата достапност
3. Прифаќајте или одбивајте барања за возење
4. Следете ги активните возења

## Технички барања
- Android 5.0 или понова верзија
- Пристап до интернет
- Овозможени локациски сервиси
- Google Play Services

## Контакт
Martin Gjorgjievski
Email: kxie1692022@feit.ukim.edu.mk
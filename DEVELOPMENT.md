# WorkMate学習記録

## 1週目(7/21~7/27)  
###  Spting Bootを使ったプロジェクトの初作成
- まず初めに動作確認をしたがファイルを作成しようとしてエラーが発生  
    →　Servletとjspを使用するものだと思っていたが.javaとHTMLで作成するのだと理解した
- 実行時にエラー発生(DB接続の設定をしていなくてエラー)
    →application propertiesにDB接続情報を記述して解決
- サンプルファイルを作成してデータの受け渡しも問題なく実行できた

  #### 学びメモ
  - `GetMapping("/hello")`で/helloにアクセスするとそのメソッドが実行される  
      → `return "hello"`;で`template/hello.html`を表示する

### 「ログイン機能」実装記録
- 入力フォームからログインIDとパスワードを受け取る  
- Spring Bootの `@PostMapping` で受け取りDBと照合  
- 一致した場合はセッション情報を作成し、メニューページへ遷移  
- 一致しない場合は、エラーメッセージを赤い文字で表示させる

  #### 学びメモ
  - `@アノテーション` により、クラスやメソッドに役割や制約を指定できる
  - `@Entity`はJavaクラスをDBテーブルに対応させる。`@Table`でテーブル名をマッピングしたり`@Column`でエンティティを定義して自動生成し   たりできる
  - `@Id`で主キー指定や`@GenerationType.IDENTITY`で主キー値を自動で割り当てる
  - `Model`はコントローラーからビューへ値を渡すための入れ物(Mapのようなもの)
  - `addAttribute("キー", 値)`で文字列やオブジェクトなどを渡せる
  - html側は`"${キー}"`で受け取る  
    (例　`model.addAttribute("title", "ログインページ")`
      → `<h1 th:text="${title}</h1>` <!-- ログインページ -->
  - `"*{キー}"`はフォーム用でth:objectで指定されたオブジェクトを基準にしてアクセスできる  
    (例　コントローラー側`addAttribute("loginForm",new LoginForm())`
      → HTML側`<form th:action="@{/login}" th:object="${loginForm}"`
     `<input type="text" th:field="*{loginId}" />`  
    これで`*{loginId}` ← `taskForm.getLoginId()`にバインドされる)  
      → 実際には`<input name="text" value="${loginForm.loginId}" />`と同じ
   - 補足：name属性・value属性・id属性(自動で生成される)を自動でセットしてくれる便利なもので  
     `<input type="text" th:field="*{loginId}" />`
     これは実際には
     `<input type="text" name="loginId" id="loginId" value="(loginForm.lodinIdの値)" />`となる  
  - `@ModelAttribute`を付与するとフォームなどから入力されたデータを指定したオブジェクトに保存してコントローラーで処理できる
  - `HttpSession` でセッション管理ができる(例：`session.setAttribute("loginUser", account)`でセッションに保存
  - `session.getAttribute("loginUser")`でセッションから値を取得
      → `Account loginUser = (Account)session.getAttribute("loginUser")`など型変換が必要
  - HTML側で使うときは`th:text="${session.loginUser.userName}"`と`session.`を使って属性にアクセス
  - `session.invalidate()`でセッション破棄


    ##### リポジトリの`extends JpaRepository<Account, Long>`の説明
    - Spring Data JPA を使ってデータベースアクセスを行う 
    - `Account`は対象となるエンティティ(テーブル)
    - `Long{はそのエンティティの主キー(@Idで指定された)の型  
    これを継承することで  
      - `findAll()` → 全件取得
      - `findById(id)` → IDで1件取得
      - `save(entity)` → 新規登録 or 更新
      - `delete(entity)` → 削除
      - `count()` → レコード数の取得  
    が使用できるようになる  
    - このインターフェースで`Account`テーブルに対して基本的なCRUD操作が使えるようになる(実装不要)  
        →　自動でSQLが作られ、ログインID検索なども簡単に書ける  
    ##### `findByLoginId(String loginId)`
    - Spting Data JPAのメソッド名でクエリ生成という仕組みを使ってloginIdで検索する処理を自動生成する  
         → SELECT * FROM account WHERE login_id = ?　を自動でやっている  


### 「新規ユーザー登録」実装記録
- 新規登録ページから入力された情報を受け取る
- `@PostMapping`で受け取りDBに同じ値があればログインIDは使用されていますと返す
- 同じ値がなければDBに保存して完了する

#### 学びメモ
- `findByLoginId(registerForm.getLoginId())`でDBからログインIDをみて`.isPresent()`で同じ値があるか確認し、あったらログインIDは使用されているとエラーを返す
- なければインスタンスを作成し受け取った情報を格納していく。
- `accountRepository.save(newUser)`で格納されているデータをDBに保存

## 2週目(7/28~8/3) 
### タスク管理機能実装記録
- ユーザーごとにタスクを表示
- 登録や編集・削除ボタンで管理
- 登録や編集などを押すとフォーム画面に遷移して入力する
- 編集機能に手間取ってこの週に実装できなかった

  #### 学びメモ
  - とりあえず最初はログインしているユーザーごとにタスクを表示させるようにした
  - エンティティに`@ManyToOne`、`@JoinColumn(name = "user_id")`でアカウントテーブルの`user_id`を外部キーにする
  - セッションに保存されている情報を取得して`findByUser(loginUser)`でユーザーIDが一致しているタスクの情報をDBから持ってきてhtml側に渡し表示する
  - 1つのコントローラーに登録、編集、削除の処理を実装した
  - 登録時にフォームから送られてきたデータをDBに保存する時userIdだけは現在のセッション情報から持ってくる
  - フォームは同じものを`form th:action=`をつかって登録か編集かを判断して表示させることもできる  
      → `form th:action="${taskForm.id == null ? @{(/tasks/create)} : @{(/tasks/edit/update)}"`でidに何も入ってなければ登録、入っていれば編集になる  
   他の記述もth:ifで分岐させてどっちかを表示されるなどが可能
  - `RequestMapping("/tasks")`で全体の共通パスになる
    `@GetMapping("/new")`などにすると/tasks/newとなる
  - `Repository`はJPAの内部機能によって決められた命名規則でメソッド名を書くと自動生成してくれる
    例 `findByLoginId(String loginId)`で渡されたログインIDを検索  
    `findByUser(Account user)`とすると`@ManyToOne`で外部キーを参照しているので自動でuser_idに変換して検索  
  - `return "redirect:/`でそのページにリダイレクト  
    redirectはhtmlではなくブラウザに指示. 
    例えば`redirect:/tasks/new`なら/tasks/newというURLにGETアクセスする  
    SpringはそのURLに対応する`@GetMapping("/tasks/new")`を探す  

## 3週目(8/4~8/10)
### タスク機能実装記録
- 編集機能の続きを実装
- タスクの検索やソート機能

  #### 学びメモ
  - フォームから渡された情報をDBに保存するとき一緒にログインしているUserIdを保存したいのでセッション情報を取得して`task.setUser(account)`などで一緒に保存
  - `th:href="@{'/tasks/edit/' + ${task.id}`という形にするとtasks/edit/1などタスクのIDの情報を含めて編集フォームに遷移できる  
      → Get /edit/{id} でマッピングして`(@PathVariable Long id)`でURLに含まれる動的パラメータを受け取りリポジトリでidをDBから探して編集フォームに渡す  
  → POST /tasks/updateなどで編集したい情報をタスクIDと共にデータを送ってDBに更新処理をする(タスクIDを送らないと新規作成になってしまう)
  - 削除を押すとタスクのIDを`@PathVariable`で受け取って`findById()`でDBからタスクを探し`teskRepository.delete()`でそれを削除する
  - Spring Securityを実装したがわからないことだらけだったのでファイルに詳細にコメントでメモをした

### ソート・検索機能実装記録
- 簡単な検索やソート機能
- 検索はタイトルにキーワードで検索して絞る
- ソートは単純に期限日に対して昇順にする
#### 学習メモ
- リンクにsortBy=""を埋め込みRequestParamで受け取る
- `List<Task> tasks = taskRepository.findAll(Sort.by(sortBy).ascending())`でsortByによって呼び出された属性にascendingで昇順にして返す
- 検索はリポジトリに`findByTitleContaining(String keyword)`などを用意しておき、フォームアクションでKeywordを受け取って絞られたタスクを返す

### Spring Securityの導入
- アクセス制限を付与(ログインしていないとアクセスできない)
- 新規登録時にパスワードをBCryptで暗号化　ログイン時にもパスワードをBCryptを使って認証
- ログアウトの処理(セッション情報の破棄、認証情報のクリアなど)
  #### 学習メモ
  - 今後ログイン情報を取得したいときは
    `@AuthenticationPrincipal AccountUserDetails userDetails`
    `Account user = userDetails.getAccount()`などで取得できる
  - SecurityConfigはセキュリティ設定クラスで、URLごとにアクセス制限やログイン関連の細かい設定、パスワード比較時のエンコード方法の設定などができる
  - `requestMatchers("/login")`などで/loginにアクセスするときは制限がない(ログインが必要ない)などの設定ができる
  - `PasswordEncoder`でBCryptなどでハッシュ化して保存、認証に使用する
  - `formLogin()`を使用するとデフォルトで`username`,`password`というフィールドを使用するのでデフォルトの名前と変えているなら`.usernameParameter()`や`.passwordParameter()`でフォームのname属性指定しないと認証がうまくいかなくなってしまうので注意
  - `DaoAuthenticationProvider`に`UserDetailsService`を登録する(情報取得サービスを使用する)
  - `UserDetailsServiceはUserDetails`を実装してログイン時に入力されたログインIDからDBのユーザ情報を探すクラス
  - ログイン時に自動で`loadUserByUsername(String loginId)`を呼び出しDBからユーザー情報を探し見つけたらUserDetailsオブジェクトに変換して返す
  - UserDetailsはUserDetailsインターフェースを実装したクラスで、Spring Securityが必要とするユーザー情報(ID、パスワード、権限など)をまとめる
## 4週目(8/11~8/17)
### ソート機能+検索機能の複合実装記録
- もう少し複雑であったら便利のような難しいものに挑戦してみたくて実装した
- タイトルやカテゴリなど特定の中でキーワードで検索して絞れる
- 作成日や期限日を昇順や降順にできる
- 上記の機能を複合して使うことも可能

#### 学習メモ
- `JpaSpecificationExecutor`をリポジトリに追加すると柔軟な動的検索ができる
- 

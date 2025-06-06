import clsx from 'clsx';
import { motion } from 'framer-motion';
import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';

import IconBack from '@/assets/icons/IconBack';
import LOGO from '@/assets/images/Character_HAo.png';

import IconLogout from '@/assets/icons/IconLogout';
import { ADMIN_NAVLIST, SUPERADMIN_NAVLIST } from '@/datas/sideBarList';
import { useLogout } from '@/service/queries/user';
import { useUserStore } from '@/stores/useUserStore';

const SideBar = () => {
  //임시 데이터
  //추후 로그인 구현 시 수정 필요
  const { user } = useUserStore();
  const IS_SUPER_ADMIN: boolean = user?.role === 'SUPER';
  const LOGIN = '관리자';
  const VERSION = '1.0.0';
  const AUTHORITY = IS_SUPER_ADMIN ? '슈퍼관리자' : '일반관리자';

  const [isSideBarOpen, setIsSideBarOpen] = useState(true);
  const location = useLocation();
  const [currentPath, setCurrentPath] = useState(location.pathname);

  // 사이드바 너비 조정 애니메이션 완료 여부
  const [isWidthAnimationComplete, setIsWidthAnimationComplete] =
    useState(true);
  const { mutate: logout } = useLogout();

  // 주소 변경 감지하여 currentPath 업데이트
  useEffect(() => {
    setCurrentPath(location.pathname);
  }, [location.pathname]);

  useEffect(() => {
    if (!isSideBarOpen) {
      setIsWidthAnimationComplete(false);
    }
  }, [isSideBarOpen]);

  return (
    <motion.nav
      initial={false}
      animate={{ width: isSideBarOpen ? '250px' : '80px' }}
      transition={{
        width: { duration: 0.3, ease: 'easeInOut' },
      }}
      onAnimationComplete={() => {
        if (isSideBarOpen) {
          setIsWidthAnimationComplete(true);
        }
      }}
      className={`h-full p-2 flex flex-col`}
    >
      <header className='h-[70px] flex justify-between items-center gap-2 font-preBold text-lg text-primary'>
        <div className='h-full flex items-center gap-2 min-w-0 text-2xl'>
          <div className='w-10 flex-shrink-0'>
            <img src={LOGO} alt='logo' className='w-full h-full' />
          </div>
          {isWidthAnimationComplete && (
            <motion.h1
              initial={{ opacity: 0, y: 12 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.125 }}
              className='truncate'
            >
              MyLio
            </motion.h1>
          )}
        </div>
        <div className='ml-4 size-6'>
          <IconBack
            className={`text-content hover:bg-gray-100 rounded-md cursor-pointer ${
              isSideBarOpen ? '' : 'rotate-180'
            }`}
            onClick={() => setIsSideBarOpen(!isSideBarOpen)}
          />
        </div>
      </header>
      <hr className='w-full' />
      <section className='flex flex-col h-[80%] min-h-[120px]'>
        {!IS_SUPER_ADMIN && (
          <ul className='h-full pt-2 flex flex-col gap-1 text-base font-preMedium'>
            {ADMIN_NAVLIST.map((item) => (
              <Link
                key={item.title}
                to={item.link}
                onClick={() => setCurrentPath(item.link)}
              >
                <li
                  className={clsx(
                    'flex items-center  rounded-md p-2',
                    currentPath === item.link
                      ? 'bg-subContent font-preBold'
                      : 'hover:bg-offWhite'
                  )}
                >
                  <div className='size-6 flex items-center justify-center'>
                    <item.icons width={20} height={20} />
                  </div>
                  <div className='ml-4'>
                    {isWidthAnimationComplete && (
                      <motion.p
                        initial={{ opacity: 0, y: 12 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.125 }}
                      >
                        {item.title}
                      </motion.p>
                    )}
                  </div>
                </li>
              </Link>
            ))}
          </ul>
        )}
        {IS_SUPER_ADMIN && (
          <ul className='h-full pt-2 flex flex-col gap-1 text-base font-preMedium'>
            {SUPERADMIN_NAVLIST.map((item) => (
              <Link
                key={item.title}
                to={item.link}
                onClick={() => setCurrentPath(item.link)}
              >
                <li
                  className={clsx(
                    'flex items-center hover:bg-offWhite rounded-md p-2',
                    currentPath === item.link && 'bg-subContent font-preBold'
                  )}
                >
                  <div className='size-6 flex items-center justify-center'>
                    <item.icons width={20} height={20} />
                  </div>
                  <div className='ml-4'>
                    {isWidthAnimationComplete && (
                      <motion.p
                        initial={{ opacity: 0, y: 12 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.125 }}
                      >
                        {item.title}
                      </motion.p>
                    )}
                  </div>
                </li>
              </Link>
            ))}
          </ul>
        )}
      </section>

      {isWidthAnimationComplete && <hr className='w-full' />}

      {isWidthAnimationComplete && (
        <div className='flex flex-col gap-2'>
          <footer
            className={`h-[10%] min-h-[80px] font-preMedium text-xs text-content p-2 flex flex-col justify-center gap-1`}
          >
            <p>로그인 : {LOGIN}</p>
            <p>버전 : {VERSION}</p>
            <p>권한 : {AUTHORITY}</p>
          </footer>
          <button onClick={() => logout()} className='flex items-center gap-2'>
            <span className='text-error text-sm font-preRegular'>로그아웃</span>
            <IconLogout width={16} height={16} />
          </button>
        </div>
      )}
    </motion.nav>
  );
};

export default SideBar;
